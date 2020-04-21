package org.zhenchao.kratos;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.converter.ListConverter;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.inject.classpath.ConfigurableOptions;
import org.zhenchao.kratos.inject.classpath.EnvironmentOptions;
import org.zhenchao.kratos.inject.classpath.NotFullyConfigurableOptions;
import org.zhenchao.kratos.inject.classpath.RawConfigurableOptions;
import org.zhenchao.kratos.inject.classpath.RawConfigurableOptions2;
import org.zhenchao.kratos.inject.classpath.RawContentOptions;
import org.zhenchao.kratos.inject.classpath.RawContentOptions2;
import org.zhenchao.kratos.listener.MockUpdateListener;
import org.zhenchao.kratos.source.PropertiesBuilderFactory;
import org.zhenchao.kratos.source.Source;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigInjectorTest extends BaseTestCase {

    private ConfigInjector injector = ConfigInjector.getInstance();

    private ConfigurableOptions options;

    @Before
    public void setup() throws ConfigException {
        options = new ConfigurableOptions();
        injector.configureBean(options);
    }

    @After
    public void tearDown() throws Exception {
        injector.reset();
    }

    @Test(expected = NullPointerException.class)
    public void testNullConfigurableOptions() throws Exception {
        this.injector.configureBean((Options) null);
    }

    @Test(expected = ConfigException.class)
    public void testMutuallyConfig1() throws Exception {
        injector.configureBean(MutuallyOptions1.class);
    }

    @Test(expected = ConfigException.class)
    public void testMutuallyConfig2() throws Exception {
        injector.configureBean(MutuallyOptions2.class);
    }

    @Test(expected = ConfigException.class)
    public void testMutuallyConfig3() throws Exception {
        injector.configureBean(MutuallyOptions3.class);
    }

    @Test
    public void testConfigurableOptions() {
        assertEquals(2, options.getFiles().length);
        assertArrayEquals(new File[] {new File("/tmp/file.txt"), new File("/tmp/text.txt")}, options.getFiles());
        assertEquals(22, options.getNumber());
        assertEquals("There is a field which states: This is a simple message - 0.34 ${along}", options.getPropMessage());
        assertEquals(1780000L, options.longValue);
        assertEquals(500L, options.anotherLongValue);
        assertEquals(123.56D, options.getFloatingPointNumber(), 0.0D);
        assertEquals("This is a simple message", options.getFieldMessage());
        assertTrue(options.getTrueFalse());
        assertEquals(0.34F, options.anotherFloat, 0.0F);
        assertEquals('S', options.getAchar());
        assertEquals((byte) 120, options.getMyByte());
        assertEquals("Hello Z Carioca!", options.getMessage());
        assertEquals(options.getNumber() + options.getFloatingPointNumber(), options.getBigNum(), 0.0D);

        assertEquals(Arrays.asList("A collection represents a group of objects, known as its elements.".split("\\s+")), options.list);
        String text = "111, 111,,, 222, 222, 3, 333, a, aaa,, bb, bbb, ccc, ccc,";
        final String[] elems = text.split(",\\s*");
        final Set<String> set = Arrays.stream(elems).collect(Collectors.toSet());
        assertEquals(set.size(), options.set.size());
        for (final String elem : elems) {
            assertTrue(options.set.contains(elem));
        }
    }

    @Test
    public void testConfigurableOptionsByClass() throws Exception {
        injector.reset();
        injector.setBuilderFactory(new PropertiesBuilderFactory());
        final ConfigurableOptions options =
                injector.configureBean(ConfigurableOptions.class).getOptions(ConfigurableOptions.class);
        assertEquals(2, options.getFiles().length);
        assertArrayEquals(new File[] {new File("/tmp/file.txt"), new File("/tmp/text.txt")}, options.getFiles());
        assertEquals(22, options.getNumber());
        assertEquals("There is a field which states: This is a simple message - 0.34 ${along}", options.getPropMessage());
        assertEquals(1780000L, options.longValue);
        assertEquals(500L, options.anotherLongValue);
        assertEquals(123.56D, options.getFloatingPointNumber(), 0.0D);
        assertEquals("This is a simple message", options.getFieldMessage());
        assertTrue(options.getTrueFalse());
        assertEquals(0.34F, options.anotherFloat, 0.0F);
        assertEquals('S', options.getAchar());
        assertEquals((byte) 120, options.getMyByte());
        assertEquals("Hello Z Carioca!", options.getMessage());
        assertEquals(options.getNumber() + options.getFloatingPointNumber(), options.getBigNum(), 0.0D);

        assertEquals(Arrays.asList("A collection represents a group of objects, known as its elements.".split("\\s+")), options.list);
        String text = "111, 111,,, 222, 222, 3, 333, a, aaa,, bb, bbb, ccc, ccc,";
        final String[] elems = text.split(",\\s*");
        final Set<String> set = Arrays.stream(elems).collect(Collectors.toSet());
        assertEquals(set.size(), options.set.size());
        for (final String elem : elems) {
            assertTrue(options.set.contains(elem));
        }
    }

    @Test
    public void testRawConfigurableOptions() throws Exception {
        final RawConfigurableOptions options =
                injector.configureBean(RawConfigurableOptions.class).getOptions(RawConfigurableOptions.class);
        assertTrue(options.getProperties().containsKey(Constants.RAW_CONFIG_KEY));
        options.getProperties().remove(Constants.RAW_CONFIG_KEY);
        assertEquals(13, options.getProperties().size());
        assertEquals("There is a field which states: This is a simple message - 0.34 ${along}",
                options.getProperties().getProperty("property.message"));
    }

    @Test
    public void testRawContentOptions() throws Exception {
        final RawContentOptions options =
                injector.configureBean(RawContentOptions.class).getOptions(RawContentOptions.class);
        assertTrue(StringUtils.isNotBlank(options.getValue()));

        final RawContentOptions2 options2 =
                injector.configureBean(RawContentOptions2.class).getOptions(RawContentOptions2.class);
        assertTrue(options2.getProperties().containsKey(Constants.RAW_CONFIG_KEY));
        assertEquals(options.getValue(), options2.getProperties().getProperty(Constants.RAW_CONFIG_KEY));
    }

    @Test
    public void testRawConfigurableOptions2() throws Exception {
        final RawConfigurableOptions2 options =
                injector.configureBean(RawConfigurableOptions2.class).getOptions(RawConfigurableOptions2.class);
        assertTrue(StringUtils.isNotBlank(options.getValue()));
    }

    @Test
    public void testNotFullyConfigurableOptions() throws Exception {
        NotFullyConfigurableOptions options = new NotFullyConfigurableOptions();
        this.injector.configureBean(options);

        assertEquals(0.34F, options.anotherFloat, 0.0F);
        assertEquals(500L, options.anotherLongValue);
        assertEquals(1780000L, options.longValue);
        assertEquals('S', options.getAchar());
        assertEquals("This is a simple message", options.getFieldMessage());
        assertEquals(new Double(123.56), options.getFloatingPointNumber());
        assertEquals("Hello Z Carioca!", options.getMessage());
        assertEquals((byte) 120, options.getMyByte());
        assertEquals(Boolean.TRUE, options.getTrueFalse());
    }

    @Test
    public void testSingleInstance() throws Exception {
        injector.configureBean(this.options);
        final ConfigurableOptions options2 =
                injector.configureBean(ConfigurableOptions.class).getOptions(ConfigurableOptions.class);
        assertSame(this.options, options2);

        final Options options3 = injector.getOptions(ConfigurableOptions.class);
        assertSame(this.options, options3);
    }

    @Test
    public void testSettingEnvironment() throws Exception {
        injector.setBuilderFactory(new PropertiesBuilderFactory(true, true));
        final EnvironmentOptions envOptions =
                injector.configureBean(EnvironmentOptions.class).getOptions(EnvironmentOptions.class);

        assertEquals("fake env value", envOptions.getEnvValue());
        assertEquals("this env value override by custom", envOptions.getEnvValue2());
        assertEquals("fake value", envOptions.getSysValue());
        assertEquals("this is a custom value", envOptions.getCusValue());
    }

    @Test(expected = ConfigException.class)
    public void testMissingRequired() throws Exception {
        injector.configureBean(MissingRequiredOptions.class);
    }

    @Test(expected = ConfigException.class)
    public void testBadCustomConverter() throws Exception {
        injector.configureBean(BadConverterOptions.class);
    }

    @Test(expected = ConfigException.class)
    public void testBadUpdateMethod() throws Exception {
        injector.configureBean(BadUpdateOptions.class);
    }

    @Test(expected = ConfigException.class)
    public void testBadValidateMethod() throws Exception {
        injector.configureBean(BadValidateOptions.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnConfigurableOptions() throws Exception {
        this.injector.configureBean(new UnConfigurableOptions());
    }

    @Test
    public void testAutoloadConfigurableOptions() throws Exception {
        final ConfigInjector injector = ConfigInjector.getInstance().reset();

        MockUpdateListener updateListener = new MockUpdateListener();
        injector.registerUpdateListener(updateListener);

        AutoloadOptions options = new AutoloadOptions();
        injector.configureBean(options);

        for (final Source source : injector.listOptionsSource()) {
            injector.reload(source);
        }
        assertEquals(2, updateListener.getCount());
        assertEquals(options, updateListener.getLastOptions());
        updateListener.setCount(0);
        updateListener.setLastOptions(null);

        for (Source source : injector.listOptionsSource()) {
            injector.reload(source);
        }
        assertEquals(1, updateListener.getCount());
        assertNotNull(updateListener.getLastOptions());
    }

    @Test
    public void testNotLoadConfigurableOptions() throws Exception {
        final ConfigInjector injector = ConfigInjector.getInstance().reset();

        MockUpdateListener updateListener = new MockUpdateListener();
        injector.registerUpdateListener(updateListener);

        NotLoadOptions options = new NotLoadOptions();
        injector.configureBean(options);
        // injector.configureBean(options);

        for (final Source source : injector.listOptionsSource()) {
            injector.reload(source);
        }
        assertEquals(1, updateListener.getCount());
        assertEquals(options, updateListener.getLastOptions());
        updateListener.setCount(0);
        updateListener.setLastOptions(null);

        for (Source source : injector.listOptionsSource()) {
            injector.reload(source);
        }
        assertEquals(0, updateListener.getCount());
        assertNull(updateListener.getLastOptions());
    }

    @Configurable("CLASSPATH:empty")
    public static class MutuallyOptions1 extends AbstractOptions {

        private static final long serialVersionUID = -5997958204637320500L;

        @Attribute(raw = true)
        private String rawField;

        @Attribute("long_value")
        private long longValue;

    }

    @Configurable("CLASSPATH:empty")
    public static class MutuallyOptions2 extends AbstractOptions {

        private static final long serialVersionUID = -5997958204637320500L;

        @Attribute(raw = true)
        private Properties rawField;

        @Attribute("long_value")
        private long longValue;

    }

    @Configurable("CLASSPATH:empty")
    public static class MutuallyOptions3 extends AbstractOptions {

        private static final long serialVersionUID = -5997958204637320500L;

        @Attribute(raw = true)
        private Properties properties;

        @Attribute(raw = true)
        private String stringValue;

    }

    @Configurable("CLASSPATH:empty")
    public static class MissingRequiredOptions extends AbstractOptions {

        private static final long serialVersionUID = -5754937380229537510L;

        @Attribute(name = "name")
        private String name;

    }

    @Configurable("CLASSPATH:configurable_options")
    public static class BadConverterOptions extends AbstractOptions {

        private static final long serialVersionUID = -5754937380229537510L;

        @Attribute(name = "list", converter = ListConverter.class)
        private String[] list;

    }

    @Configurable(Constants.CP_PREFIX + "configurable_options")
    public static class BadUpdateOptions extends ConfigurableOptions {

        private static final long serialVersionUID = -3463436030227007221L;

        public BadUpdateOptions() {
        }

        @Override
        public void update() {
            throw new IllegalStateException("Bad update method.");
        }

    }

    @Configurable(Constants.CP_PREFIX + "configurable_options")
    public static class BadValidateOptions extends ConfigurableOptions {

        private static final long serialVersionUID = -3463436030227007221L;

        public BadValidateOptions() {
        }

        @Override
        public boolean validate() {
            return false;
        }
    }

    public static class UnConfigurableOptions extends AbstractOptions {

        private static final long serialVersionUID = -5623663054808993307L;
    }

    @Configurable("CLASSPATH:autoload")
    public static class AutoloadOptions extends AbstractOptions {

        private static final long serialVersionUID = -2561088257509980932L;
    }

    @Configurable("CLASSPATH:notload")
    public static class NotLoadOptions extends AbstractOptions {

        private static final long serialVersionUID = -6067738678459387410L;
    }

}
