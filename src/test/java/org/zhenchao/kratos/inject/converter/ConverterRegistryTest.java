package org.zhenchao.kratos.inject.converter;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterRegistryTest {

    private ConverterRegistry registry = ConverterRegistry.getInstance();

    @After
    public void tearDown() throws Exception {
        registry.reset();
    }

    @Test
    public void testGetRegistry() {
        assertNotNull(ConverterRegistry.getInstance());
        assertSame(ConverterRegistry.getInstance(), ConverterRegistry.getInstance());
    }

    @Test
    public void testBuiltInRegister() {
        registry.register(new Converter<SimpleDateFormat>() {
            @Override
            public Class<SimpleDateFormat> supportedClass() {
                return SimpleDateFormat.class;
            }

            @Override
            public SimpleDateFormat convert(String value, BeanProperty property) throws ConvertException {
                return new SimpleDateFormat(value);
            }
        });

        assertNotNull(registry.getBuiltInConverter(SimpleDateFormat.class));
        assertEquals(SimpleDateFormat.class, registry.getBuiltInConverter(SimpleDateFormat.class).supportedClass());
        assertEquals(Object.class, registry.getBuiltInConverter(File.class).supportedClass());
    }

    @Test
    public void testCustomRegister() {
        final ListConverter converter = new ListConverter();

        registry.register(converter, true);

        assertNotNull(registry.getCustomConverter(converter.getClass()));
        assertSame(registry.getCustomConverter(converter.getClass()), registry.getCustomConverter(converter.getClass()));
        assertEquals(List.class, registry.getCustomConverter(converter.getClass()).supportedClass());
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterNull() throws ConfigException {
        registry.register(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterBad() throws ConfigException {
        registry.register(new Converter<SimpleDateFormat>() {
            @Override
            public Class<SimpleDateFormat> supportedClass() {
                return null;
            }

            @Override
            public SimpleDateFormat convert(String value, BeanProperty property) throws ConvertException {
                return null;
            }
        });
    }

    @Test
    public void testGetBuiltInConverter() {
        assertEquals(Boolean.class, registry.getBuiltInConverter(boolean.class).supportedClass());
        assertEquals(Boolean.class, registry.getBuiltInConverter(Boolean.class).supportedClass());
        assertEquals(Character.class, registry.getBuiltInConverter(char.class).supportedClass());
        assertEquals(Character.class, registry.getBuiltInConverter(Character.class).supportedClass());
        assertEquals(Byte.class, registry.getBuiltInConverter(byte.class).supportedClass());
        assertEquals(Byte.class, registry.getBuiltInConverter(Byte.class).supportedClass());
        assertEquals(Short.class, registry.getBuiltInConverter(short.class).supportedClass());
        assertEquals(Short.class, registry.getBuiltInConverter(Short.class).supportedClass());
        assertEquals(Integer.class, registry.getBuiltInConverter(int.class).supportedClass());
        assertEquals(Integer.class, registry.getBuiltInConverter(Integer.class).supportedClass());
        assertEquals(Long.class, registry.getBuiltInConverter(long.class).supportedClass());
        assertEquals(Long.class, registry.getBuiltInConverter(Long.class).supportedClass());
        assertEquals(Float.class, registry.getBuiltInConverter(float.class).supportedClass());
        assertEquals(Float.class, registry.getBuiltInConverter(Float.class).supportedClass());
        assertEquals(Double.class, registry.getBuiltInConverter(double.class).supportedClass());
        assertEquals(Double.class, registry.getBuiltInConverter(Double.class).supportedClass());
        assertEquals(String.class, registry.getBuiltInConverter(String.class).supportedClass());
        assertEquals(Object.class, registry.getBuiltInConverter(File.class).supportedClass());
        assertEquals(Object.class, registry.getBuiltInConverter(SimpleDateFormat.class).supportedClass());
        assertEquals(Object.class, registry.getBuiltInConverter(Object.class).supportedClass());
        assertEquals(Date.class, registry.getBuiltInConverter(Date.class).supportedClass());
        assertEquals(Calendar.class, registry.getBuiltInConverter(Calendar.class).supportedClass());
    }

    @Test
    public void testGetBuiltInConverterForArray() {
        assertEquals((new boolean[0]).getClass(), registry.getBuiltInConverter((new boolean[0]).getClass()).supportedClass());
        assertEquals((new Boolean[0]).getClass(), registry.getBuiltInConverter((new Boolean[0]).getClass()).supportedClass());
        assertEquals((new char[0]).getClass(), registry.getBuiltInConverter((new char[0]).getClass()).supportedClass());
        assertEquals((new Character[0]).getClass(), registry.getBuiltInConverter((new Character[0]).getClass()).supportedClass());
        assertEquals((new byte[0]).getClass(), registry.getBuiltInConverter((new byte[0]).getClass()).supportedClass());
        assertEquals((new Byte[0]).getClass(), registry.getBuiltInConverter((new Byte[0]).getClass()).supportedClass());
        assertEquals((new short[0]).getClass(), registry.getBuiltInConverter((new short[0]).getClass()).supportedClass());
        assertEquals((new Short[0]).getClass(), registry.getBuiltInConverter((new Short[0]).getClass()).supportedClass());
        assertEquals((new int[0]).getClass(), registry.getBuiltInConverter((new int[0]).getClass()).supportedClass());
        assertEquals((new Integer[0]).getClass(), registry.getBuiltInConverter((new Integer[0]).getClass()).supportedClass());
        assertEquals((new long[0]).getClass(), registry.getBuiltInConverter((new long[0]).getClass()).supportedClass());
        assertEquals((new Long[0]).getClass(), registry.getBuiltInConverter((new Long[0]).getClass()).supportedClass());
        assertEquals((new float[0]).getClass(), registry.getBuiltInConverter((new float[0]).getClass()).supportedClass());
        assertEquals((new Float[0]).getClass(), registry.getBuiltInConverter((new Float[0]).getClass()).supportedClass());
        assertEquals((new double[0]).getClass(), registry.getBuiltInConverter((new double[0]).getClass()).supportedClass());
        assertEquals((new Double[0]).getClass(), registry.getBuiltInConverter((new Double[0]).getClass()).supportedClass());
        assertEquals((new String[0]).getClass(), registry.getBuiltInConverter((new String[0]).getClass()).supportedClass());
        assertEquals((new File[0]).getClass(), registry.getBuiltInConverter((new File[0]).getClass()).supportedClass());
        assertEquals((new SimpleDateFormat[0]).getClass(), registry.getBuiltInConverter((new SimpleDateFormat[0]).getClass()).supportedClass());
        assertEquals((new Object[0]).getClass(), registry.getBuiltInConverter((new Object[0]).getClass()).supportedClass());
        assertEquals((new Date[0]).getClass(), registry.getBuiltInConverter((new Date[0]).getClass()).supportedClass());
        assertEquals((new Calendar[0]).getClass(), registry.getBuiltInConverter((new Calendar[0]).getClass()).supportedClass());
    }

    @Test(expected = NullPointerException.class)
    public void testGetBuiltInConverterWithNull() {
        registry.getBuiltInConverter(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCustomConverterWithNull() {
        registry.getCustomConverter(null);
    }

    public static class ListConverter implements Converter<List<Integer>> {

        @Override
        public List<Integer> convert(String value, BeanProperty property) throws ConvertException {
            return Arrays.stream(value.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toList());
        }

        @Override
        public Class<?> supportedClass() {
            return List.class;
        }
    }

}
