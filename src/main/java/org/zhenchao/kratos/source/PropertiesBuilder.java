package org.zhenchao.kratos.source;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.zhenchao.kratos.util.ConfUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhenchao.wang 2017-09-06 13:33
 * @version 1.0.0
 */
public class PropertiesBuilder {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{[^\\s]+\\}");

    private final Map<String, String> props;
    private Environment environment;
    private boolean build;

    public PropertiesBuilder() {
        this.environment = EnvironmentAccessor.getInstance().getEnvironment();
        this.props = new HashMap<>();
        this.build = false;
    }

    /**
     * Add a environment property to builder, ignore if not exist in system env.
     *
     * @param name
     * @return
     */
    public PropertiesBuilder addEnvironmentProperty(String name) {
        return this.addEnvironmentProperty(name, null);
    }

    /**
     * Add a environment property to builder with default value, ignore if not exist in system env.
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public PropertiesBuilder addEnvironmentProperty(String name, String defaultValue) {
        Validate.notBlank(name, "blank name");
        final String trimName = name.trim();
        final String value = environment.getEnvironmentProperty(trimName, defaultValue);
        if (null != value) {
            return this.addProperty(trimName, value);
        }
        return this;
    }

    /**
     * Add a system property to builder, ignore if not exist in system properties.
     *
     * @param name
     * @return
     */
    public PropertiesBuilder addSystemProperty(String name) {
        return this.addSystemProperty(name, null);
    }

    /**
     * Add a system property to builder with default value, ignore if not exist in system properties.
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public PropertiesBuilder addSystemProperty(String name, String defaultValue) {
        Validate.notBlank(name, "blank name");
        final String trimName = name.trim();
        final String value = environment.getSystemProperty(trimName, defaultValue);
        if (null != value) {
            return this.addProperty(trimName, value);
        }
        return this;
    }

    /**
     * Add a property to builder.
     *
     * @param name
     * @param value
     * @return
     */
    public PropertiesBuilder addProperty(String name, String value) {
        Validate.notBlank(name, "blank name");
        Validate.notNull(value, "null value");
        if (build) {
            throw new IllegalStateException(
                    "already built, and can't add property, name: " + name + ", value: " + value);
        }
        final String trimName = name.trim();
        this.props.put(trimName, value.trim());
        return this;
    }

    /**
     * Add all environment properties to builder.
     *
     * @return
     */
    public PropertiesBuilder addAllEnvironmentProperties() {
        return this.addAll(environment.getEnvironmentProperties());
    }

    /**
     * Add all system properties to builder.
     *
     * @return
     */
    public PropertiesBuilder addAllSystemProperties() {
        return this.addAll(environment.getSystemProperties());
    }

    /**
     * Add all properties to builder.
     *
     * @param props
     * @return
     */
    public PropertiesBuilder addAll(Map<String, String> props) {
        props.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getKey()))
                // .filter(entry -> StringUtils.isNotEmpty(entry.getValue()))
                .forEach(entry -> this.addProperty(entry.getKey(), entry.getValue()));
        return this;
    }

    /**
     * Load from {@link InputStream}.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public PropertiesBuilder readFromInputStream(final InputStream inputStream) throws IOException {
        Validate.notNull(inputStream, "null input stream");
        Properties props = new Properties();
        props.load(inputStream);
        this.addAll(ConfUtils.toMap(props));
        return this;
    }

    public Properties build() {
        if (build) {
            throw new IllegalStateException("already built");
        }
        this.build = true;
        Properties properties = new Properties();
        for (String key : this.props.keySet()) {
            properties.setProperty(key, this.getFilterValue(key));
        }
        return properties;
    }

    int size() {
        if (build) {
            throw new IllegalStateException("The properties object for this builder has already been built.");
        }
        return this.props.size();
    }

    String getProperty(String name) {
        if (build) {
            throw new IllegalStateException("The properties object for this builder has already been built.");
        }
        return this.props.get(name);
    }

    String getFilterValue(String key) {
        String value = StringUtils.trimToEmpty(this.props.get(key));
        Matcher matcher = PATTERN.matcher(value);
        while (matcher.find()) {
            String group = matcher.group();
            String valKey = group.substring(2, group.length() - 1);

            String prop = this.getFilterValue(valKey);
            if (StringUtils.isNotEmpty(prop)) {
                value = matcher.replaceFirst(prop);
                matcher = PATTERN.matcher(value);
            }
        }
        return value;
    }

}
