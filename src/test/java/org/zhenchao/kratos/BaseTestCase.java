package org.zhenchao.kratos;

import org.junit.BeforeClass;
import org.zhenchao.kratos.source.Environment;
import org.zhenchao.kratos.source.EnvironmentAccessor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BaseTestCase {

    @BeforeClass
    public static void setupMockEnvironment() {
        EnvironmentAccessor.getInstance().setEnvironment(new MockEnvironment());
    }

    public static class MockEnvironment implements Environment {

        private final Map<String, String> env = new HashMap<>();
        private final Map<String, String> sys = new HashMap<>();

        public MockEnvironment() {
            env.put("fake-env-prop", "fake env value");
            env.put("fake-env-prop-2", "fake env value 2");
            env.put("APP_ROOT", new File(System.getProperty("java.io.tmpdir"), "app_root").getAbsolutePath());

            sys.put("fake.system.property", "fake value");
        }

        @Override
        public Map<String, String> getEnvironmentProperties() {
            return env;
        }

        @Override
        public Map<String, String> getSystemProperties() {
            return sys;
        }

        @Override
        public String getEnvironmentProperty(String name) {
            return this.getEnvironmentProperty(name, null);
        }

        @Override
        public String getSystemProperty(String name) {
            return this.getSystemProperty(name, null);
        }

        @Override
        public String getEnvironmentProperty(String name, String defaultValue) {
            if (env.containsKey(name)) {
                return env.get(name);
            }
            return defaultValue;
        }

        @Override
        public String getSystemProperty(String name, String defaultValue) {
            if (sys.containsKey(name)) {
                return sys.get(name);
            }
            return defaultValue;
        }

        @Override
        public boolean containsSystemProperty(String name) {
            return sys.containsKey(name);
        }

        @Override
        public boolean containsEnvironmentProperty(String name) {
            return env.containsKey(name);
        }
    }

}
