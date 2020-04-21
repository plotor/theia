package org.zhenchao.kratos.source;

import org.zhenchao.kratos.anno.TestingVisible;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zhenchao.wang 2016-09-06 14:36:14
 * @version 1.0.0
 */
public class EnvironmentAccessor {

    private static final EnvironmentAccessor INSTANCE = new EnvironmentAccessor();

    private Environment environment;

    private EnvironmentAccessor() {
        this.environment = new DefaultEnvironment();
    }

    public static EnvironmentAccessor getInstance() {
        return INSTANCE;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    @TestingVisible
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Default {@link Environment} impl.
     */
    private static class DefaultEnvironment implements Environment {

        @Override
        public Map<String, String> getEnvironmentProperties() {
            return Collections.unmodifiableMap(System.getenv());
        }

        @Override
        public Map<String, String> getSystemProperties() {
            Map<String, String> result = new HashMap<>();
            for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
                result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            return Collections.unmodifiableMap(result);
        }

        @Override
        public String getEnvironmentProperty(String name) {
            return this.getEnvironmentProperty(name, null);
        }

        @Override
        public String getEnvironmentProperty(String name, String defaultValue) {
            final String value = System.getenv(name);
            return null == value ? defaultValue : value;
        }

        @Override
        public String getSystemProperty(String name) {
            return System.getProperty(name);
        }

        @Override
        public String getSystemProperty(String name, String defaultValue) {
            return System.getProperty(name, defaultValue);
        }

        @Override
        public boolean containsSystemProperty(String name) {
            return null != this.getSystemProperty(name);
        }

        @Override
        public boolean containsEnvironmentProperty(String name) {
            return null != this.getEnvironmentProperty(name);
        }
    }
}
