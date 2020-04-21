package org.zhenchao.kratos.source;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class EnvironmentAccessorTest {

    private Environment environment;

    @Before
    public void setupEnvironment() {
        environment = EnvironmentAccessor.getInstance().getEnvironment();
    }

    @Test
    public void testGetAllEnvProperties() {
        Map<String, String> env = environment.getEnvironmentProperties();
        assertNotNull(env);
        assertFalse(env.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllEnvPropertiesImmutable() {
        Map<String, String> env = environment.getEnvironmentProperties();
        env.clear();
    }

    @Test
    public void testGetAllSysProperties() {
        Map<String, String> sys = environment.getSystemProperties();
        assertNotNull(sys);
        assertFalse(sys.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllSysPropertiesImmutable() {
        Map<String, String> sys = environment.getSystemProperties();
        sys.clear();
    }

    @Test
    public void testGetEnvVariable() {
        assertNull(environment.getEnvironmentProperty(RandomStringUtils.randomAlphanumeric(8)));
    }

    @Test
    public void testGetEnvVariableWithDefault() {
        assertEquals("fake value", environment.getEnvironmentProperty(RandomStringUtils.randomAlphanumeric(8), "fake value"));
    }

    @Test
    public void testGetSysProperty() {
        assertNull(environment.getSystemProperty(RandomStringUtils.randomAlphanumeric(8)));
        assertNotNull(environment.getSystemProperty("java.io.tmpdir"));
    }

    @Test
    public void testGetSysPropertyWithDefault() {
        String randomKey = RandomStringUtils.randomAlphanumeric(128);
        assertEquals("fake value", environment.getSystemProperty(randomKey, "fake value"));

        System.setProperty(randomKey, "value123");
        assertEquals("value123", environment.getSystemProperty(randomKey, "fake value"));
        System.clearProperty(randomKey);
    }

}
