package org.zhenchao.kratos;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.error.ConfigException;

import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author zhenchao.wang 2020-01-17 19:10
 * @version 1.0.0
 */
public class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);

    private static final ConfigManager INSTANCE = new ConfigManager();

    private volatile boolean initialized = false;

    private final ConfigInjector injector = ConfigInjector.getInstance();

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public int initialize() throws ConfigException {
        return this.initialize("");
    }

    /**
     * Initialize the configuration, all {@link Options}s will be injection,
     * exclude the option who's {@link Configurable#autoConfigure()} is false.
     *
     * @param rootPackage the root package of reflection scanner
     * @return number of injected options
     * @throws ConfigException
     */
    public synchronized int initialize(final String rootPackage) throws ConfigException {
        if (initialized) {
            throw new ConfigException("already initialized");
        }
        int count = 0;
        Reflections reflections = StringUtils.isBlank(rootPackage) ? new Reflections() : new Reflections(rootPackage);
        final Set<Class<? extends Options>> types = reflections.getSubTypesOf(Options.class);
        for (final Class<? extends Options> optionsType : types) {
            if (Modifier.isAbstract(optionsType.getModifiers())) {
                continue;
            }

            if (!optionsType.isAnnotationPresent(Configurable.class)) {
                throw new ConfigException("missing @" + Configurable.class.getSimpleName() + " annotations: " + optionsType);
            }

            final Configurable configurable = optionsType.getAnnotation(Configurable.class);
            if (configurable.autoConfigure()) {
                if (injector.isConfigured(optionsType)) {
                    log.debug("Configured options[{}], and skip duplicated registration.", optionsType);
                } else {
                    log.info("Auto configure options: {}", optionsType);
                    injector.configureBean(optionsType);
                    count++;
                }
            } else {
                log.warn("You may want to configure the options[{}] by manual, and skip it.", optionsType);
            }
        }
        this.initialized = true;
        return count;
    }

    /**
     * Get options instance by class.
     *
     * @param optionsClass
     * @param <T>
     * @return
     */
    public <T extends Options> T getOptions(Class<T> optionsClass) {
        return injector.getOptions(optionsClass);
    }

    /**
     * Reset configuration, this method will clear all configuration state.
     */
    public synchronized void reset() {
        this.initialized = false;
        injector.reset();
    }

    public ConfigInjector getInjector() {
        return injector;
    }
}
