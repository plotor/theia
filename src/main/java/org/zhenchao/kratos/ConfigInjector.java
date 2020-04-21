package org.zhenchao.kratos;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.anno.TestingVisible;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.inject.PropertyInjector;
import org.zhenchao.kratos.inject.PropertyInjectorDetector;
import org.zhenchao.kratos.listener.InjectEventListener;
import org.zhenchao.kratos.listener.UpdateEventListener;
import org.zhenchao.kratos.source.PropertiesBuilderFactory;
import org.zhenchao.kratos.source.Source;
import org.zhenchao.kratos.source.provider.ProviderFactory;
import org.zhenchao.kratos.source.provider.SourceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhenchao.wang 2016-12-01 14:22:31
 * @version 1.0.0
 */
public class ConfigInjector {

    private static final Logger log = LoggerFactory.getLogger(ConfigInjector.class);

    private static final ConfigInjector INSTANCE = new ConfigInjector();

    private ProviderFactory providerFactory = ProviderFactory.getInstance();

    private PropertiesBuilderFactory builderFactory = new PropertiesBuilderFactory();

    private volatile boolean shutdown;

    private final Map<Class<? extends Options>, Options> optionsMap = new ConcurrentHashMap<>();
    private final Map<Source, Options> sourceMap = new ConcurrentHashMap<>();

    private final Set<InjectEventListener> injectListeners = new HashSet<>();
    private final Set<UpdateEventListener> updateListeners = new HashSet<>();

    private ConfigInjector() {
        this.shutdown = false;
    }

    public static ConfigInjector getInstance() {
        return INSTANCE;
    }

    /**
     * Reset the state of the {@link ConfigInjector}.
     *
     * @return
     */
    public synchronized ConfigInjector reset() {
        optionsMap.clear();
        sourceMap.clear();
        injectListeners.clear();
        updateListeners.clear();
        return INSTANCE;
    }

    public <T extends Options> ConfigInjector configureBean(final Class<T> optionsClass) throws ConfigException {
        Validate.notNull(optionsClass, "null options class");
        synchronized (optionsClass) {
            Options options = optionsMap.get(optionsClass);
            if (null != options) {
                return this;
            }
            try {
                log.info("Instantiate options: {}", optionsClass);
                T instance = optionsClass.newInstance();
                return this.configureBean(instance);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new ConfigException(
                        "create options bean instance error, class: " + optionsClass, e);
            }
        }
    }

    public ConfigInjector configureBean(final Options options) throws ConfigException {
        return this.configureBean(options, new Source(options));
    }

    /**
     * Inject the target options bean by {@link Source} object.
     *
     * @param options target options bean instance
     * @param source data source
     * @throws ConfigException
     */
    public ConfigInjector configureBean(final Options options, final Source source) throws ConfigException {
        Validate.notNull(options, "null inject bean");
        Validate.notNull(source, "null source");

        if (!options.getClass().equals(source.getOptionsClass())) {
            throw new ConfigException("options class mismatch, " +
                    "expected: " + options.getClass().getSimpleName() +
                    ", but found in source is " + source.getOptionsClass().getSimpleName());
        }

        synchronized (options.getClass()) {
            if (this.injectBean(options, source, false)) {
                this.doUpdate(options);
            }
        }

        return this;
    }

    /**
     * Get the options instance, which is managed by {@link ConfigInjector}.
     *
     * @param optionsClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Options> T getOptions(final Class<T> optionsClass) {
        synchronized (optionsClass) {
            return (T) optionsMap.get(optionsClass);
        }
    }

    /**
     * Check if the specify options is configured.
     *
     * @param optionsClass
     * @return
     */
    public boolean isConfigured(final Class<? extends Options> optionsClass) {
        return optionsMap.containsKey(optionsClass);
    }

    /**
     * Invoke {@link InjectEventListener#prevHandle(Options)} before do inject.
     *
     * @param options
     */
    private void prevInject(final Options options) {
        log.debug("Pre-handle before inject options[{}].", options.getClass());
        for (InjectEventListener listener : this.injectListeners) {
            listener.prevHandle(options);
        }
    }

    /**
     * Invoke {@link InjectEventListener#postHandle(Options)} after do inject.
     *
     * @param options
     */
    private void postInject(final Options options) {
        log.debug("Post-handle after inject options[{}].", options.getClass());
        for (InjectEventListener listener : this.injectListeners) {
            listener.postHandle(options);
        }
    }

    /**
     * Do injection.
     *
     * @param options
     * @param source
     * @param reload
     * @return {@code true} means injected, or means skip for some reasons.
     * @throws ConfigException
     */
    private boolean injectBean(final Options options, final Source source, boolean reload) throws ConfigException {
        Validate.isTrue(options.getClass().equals(source.getOptionsClass()),
                "unexpected options class, expected: " + options.getClass().getSimpleName() +
                        ", but got: " + source.getOptionsClass().getSimpleName());

        log.info("Inject options bean[{}], resource[{}]", options.getClass().getSimpleName(), source.getResourceName());
        SourceProvider provider = providerFactory.getSourceProvider(source);
        if (null == provider) {
            throw new IllegalStateException("no source provider was defined for source: " + source);
        }

        boolean needInject = false;
        try {
            final Properties properties = provider.loadProperties(source, builderFactory.newPropertiesBuilder());
            needInject = !reload
                    || source.isAutoload()
                    || BooleanUtils.toBoolean(properties.getProperty(Constants.COMMONS_CONFIG_AUTOLOAD, "false"));
            if (!needInject) {
                log.warn("[CONF AUTOLOAD DISABLE] skip reload, source[{}].", source);
                return false;
            }

            if (reload) {
                log.info("[CONF AUTOLOAD ENABLE] do inject, sourceAutoload[{}]", source.isAutoload());
            }

            // try inject
            this.tryInjectProperties(options, source, properties);

            this.prevInject(options);
            this.injectProperties(options, properties);
            this.registerOptions(source, options);
            return true;
        } catch (ConfigException e) {
            log.error("Load or inject configuration to options error, source[{}].", source, e);
            throw e;
        } finally {
            if (needInject) {
                this.postInject(options);
            }
        }
    }

    private void tryInjectProperties(final Options options, final Source source, final Properties properties) throws ConfigException {
        try {
            log.info("Try inject and verify the options' copy, " +
                    "options: {}, resource: {}.", source.getOptionsClass().getSimpleName(), source.getResourceName());
            Options copyOptions = SerializationUtils.clone(options);
            this.injectProperties(copyOptions, properties);
            if (!copyOptions.validate()) {
                throw new ConfigException("invalid configuration, " +
                        "options: " + source.getOptionsClass().getSimpleName() + ", resource: " + source.getResourceName() + ", and ignore it");
            }
        } catch (Throwable t) {
            if (t instanceof ConfigException) {
                throw t;
            }
            throw new ConfigException("inject configuration error, " +
                    "options: " + source.getOptionsClass().getSimpleName() + ", resource: " + source.getResourceName() + ", and ignore it",
                    t);
        }
    }

    private void injectProperties(final Options options, final Properties properties) throws ConfigException {
        Collection<PropertyInjector> propertyInjectors = PropertyInjectorDetector.resolveInjectors(options);

        // do field injection
        for (final PropertyInjector injector : propertyInjectors) {
            injector.injectValue(properties);
        }
    }

    /**
     * Reload configuration, this will be invoked by listener.
     *
     * @param source
     * @throws ConfigException
     */
    public void reload(final Source source) throws ConfigException {
        final Class<? extends Options> optionsClass = source.getOptionsClass();
        synchronized (optionsClass) {
            final Options options = this.sourceMap.get(source);
            if (null == options) {
                throw new ConfigException("no options instance found, source: " + source);
            }
            log.info("Refresh the options[{}], source[{}].", optionsClass.getSimpleName(), source);
            if (this.injectBean(options, source, true)) {
                this.doUpdate(options);
            }
        }
    }

    /**
     * Invoke {@link Options#update()} method.
     *
     * @param options
     * @throws ConfigException
     */
    private void doUpdate(final Options options) throws ConfigException {
        log.info("Invoke options update method, options[{}].", options.getClass().getSimpleName());
        try {
            this.prevUpdate(options);
            options.update();
        } catch (Throwable t) {
            throw new ConfigException(
                    "invoke options update method error: " + options.getClass(), t);
        } finally {
            this.postUpdate(options);
        }
    }

    private void prevUpdate(final Options options) {
        log.debug("Pre-handle before update options[{}].", options.getClass());
        for (UpdateEventListener listener : this.updateListeners) {
            listener.prevHandle(options);
        }
    }

    private void postUpdate(final Options options) {
        log.debug("Post-handle after update options[{}].", options.getClass());
        for (UpdateEventListener listener : this.updateListeners) {
            listener.postHandle(options);
        }
    }

    public void setBuilderFactory(PropertiesBuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public synchronized void shutdown() {
        log.info("Shutdown commons config injector.");
        if (shutdown) {
            return;
        }
        providerFactory.clear();
        optionsMap.clear();
        sourceMap.clear();
        injectListeners.clear();
        updateListeners.clear();
        shutdown = true;
    }

    public ConfigInjector registerInjectListener(InjectEventListener listener) {
        Validate.notNull(listener, "null inject event listener");
        synchronized (injectListeners) {
            injectListeners.add(listener);
        }
        return this;
    }

    public boolean removeInjectListener(InjectEventListener listener) {
        Validate.notNull(listener, "null inject event listener");
        synchronized (injectListeners) {
            return injectListeners.remove(listener);
        }
    }

    public ConfigInjector registerUpdateListener(UpdateEventListener listener) {
        Validate.notNull(listener, "null update event listener");
        synchronized (updateListeners) {
            updateListeners.add(listener);
        }
        return this;
    }

    public boolean removeUpdateListener(UpdateEventListener listener) {
        Validate.notNull(listener, "null update event listener");
        synchronized (updateListeners) {
            return updateListeners.remove(listener);
        }
    }

    @TestingVisible
    public Collection<Source> listOptionsSource() {
        return this.sourceMap.keySet();
    }

    private void registerOptions(final Source source, final Options options) {
        if (optionsMap.containsKey(options.getClass()) && optionsMap.get(options.getClass()) != options) {
            throw new IllegalStateException("found duplicated registry for options: " + options.getClass());
        }
        optionsMap.put(options.getClass(), options);

        if (sourceMap.containsKey(source) && sourceMap.get(source) != options) {
            throw new IllegalStateException("found duplicated registry for source: " + source.getClass());
        }
        sourceMap.put(source, options);
    }

}
