package org.zhenchao.kratos.source.provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.zhenchao.kratos.Options;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.source.PropertiesBuilder;
import org.zhenchao.kratos.source.Source;
import org.zhenchao.kratos.util.ConfUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Load configuration from classpath, this implementation is often used for testing.
 *
 * @author zhenchao.wang 2016-09-06 14:13:25
 * @version 1.0.0
 */
public class ClasspathSourceProvider extends AbstractSourceProvider {

    @Override
    protected Properties doLoadProperties(final Source source, final PropertiesBuilder builder) throws ConfigException {
        final Class<? extends Options> optionsClass = source.getOptionsClass();
        final String resourceName = this.resourceName(source);
        final String filepath = resourceName.substring(Constants.CP_PREFIX.length()).trim();
        try (InputStream inputStream = this.getResourceAsStream(optionsClass, filepath)) {
            final Properties properties = this.toProperties(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
            if (!properties.isEmpty()) {
                builder.addAll(ConfUtils.toMap(properties));
            }
            return builder.build();
        } catch (Throwable t) {
            throw new ConfigException(
                    String.format("can't read data from %s, options: %s", resourceName, optionsClass), t);
        }
    }

    @Override
    protected String resourceName(final Source source) {
        String resourceName = StringUtils.trimToEmpty(source.getResourceName());
        Validate.isTrue(ConfUtils.isClassPathResource(resourceName),
                "invalid classpath resource name: " + resourceName);
        if (resourceName.endsWith(".properties")) {
            return resourceName;
        }
        return resourceName + ".properties";
    }

    @Override
    public boolean support(final Source source) {
        try {
            final String resourceName = this.resourceName(source);
            return ConfUtils.isClassPathResource(resourceName);
        } catch (Throwable t) {
            // ignore it
        }
        return false;
    }

    @Override
    public int priority() {
        return 0;
    }

    private InputStream getResourceAsStream(Class<?> referenceClass, String filepath) {
        log.debug("Get resource as stream, path[{}].", filepath);
        InputStream stream = referenceClass.getResourceAsStream(filepath);
        if (null == stream) {
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
        }
        if (null == stream) {
            throw new IllegalStateException("get resource as null stream: " + filepath);
        }
        return stream;
    }
}