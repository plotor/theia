package org.zhenchao.kratos.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.zhenchao.kratos.BaseTestCase;

public class PropertiesBuilderFactoryTest extends BaseTestCase {

    @Test
    public void testPropertiesBuilderFactory() {
        PropertiesBuilderFactory factory = new PropertiesBuilderFactory();
        assertFalse(factory.isAddEnvProperties());
        assertFalse(factory.isAddSysProperties());
    }

    @Test
    public void testPropertiesBuilderFactoryFalseFalse() {
        PropertiesBuilderFactory factory = new PropertiesBuilderFactory(false, false);
        assertFalse(factory.isAddEnvProperties());
        assertFalse(factory.isAddSysProperties());
    }

    @Test
    public void testPropertiesBuilderFactoryFalseTrue() {
        PropertiesBuilderFactory factory = new PropertiesBuilderFactory(false, true);
        assertFalse(factory.isAddEnvProperties());
        assertTrue(factory.isAddSysProperties());
    }

    @Test
    public void testPropertiesBuilderFactoryTrueFalse() {
        PropertiesBuilderFactory factory = new PropertiesBuilderFactory(true, false);
        assertTrue(factory.isAddEnvProperties());
        assertFalse(factory.isAddSysProperties());
    }

    @Test
    public void testPropertiesBuilderFactoryTrueTrue() {
        PropertiesBuilderFactory factory = new PropertiesBuilderFactory(true, true);
        assertTrue(factory.isAddEnvProperties());
        assertTrue(factory.isAddSysProperties());
    }

    @Test
    public void testNewPropertiesBuilder() {
        PropertiesBuilder emptyBuilder = new PropertiesBuilderFactory().newPropertiesBuilder();
        assertEquals(0, emptyBuilder.size());

        PropertiesBuilder envBuilder = new PropertiesBuilderFactory(true, false).newPropertiesBuilder();
        assertEquals(3, envBuilder.size());

        PropertiesBuilder fullBuilder = new PropertiesBuilderFactory(true, true).newPropertiesBuilder();
        assertEquals(4, fullBuilder.size());
    }

}
