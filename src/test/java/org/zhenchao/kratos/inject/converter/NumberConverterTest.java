package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.NumberRadix.NumberFormat;
import org.zhenchao.kratos.error.ConvertException;

public class NumberConverterTest extends ConverterTestSupport {

    @Test
    public void testByte() throws Exception {
        NumberConverter<Byte> converter = NumberConverter.newConverter(Byte.class);
        assertEquals(0, converter.convert("0", beanProperty).byteValue());
        assertEquals(-128, converter.convert("-128", beanProperty).byteValue());
        assertEquals(127, converter.convert("127", beanProperty).byteValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testByteOutOfRange() throws Exception {
        NumberConverter<Byte> converter = NumberConverter.newConverter(Byte.class);
        converter.convert("255", beanProperty);
    }

    @Test
    public void testShort() throws Exception {
        NumberConverter<Short> converter = NumberConverter.newConverter(Short.class);
        assertEquals(0, converter.convert("0", beanProperty).shortValue());
        assertEquals(567, converter.convert("567", beanProperty).shortValue());
        assertEquals(-2567, converter.convert("-2567", beanProperty).shortValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testShortOutOfRange() throws Exception {
        NumberConverter<Short> converter = NumberConverter.newConverter(Short.class);
        converter.convert("99999999999", beanProperty);
    }

    @Test
    public void testInteger() throws Exception {
        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(567000, converter.convert("567000", beanProperty).intValue());
        assertEquals(-2003567, converter.convert("-2003567", beanProperty).intValue());
        assertEquals(2000000000, converter.convert("2000000000", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithNoNumberFormat() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(null));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(567000, converter.convert("567000", beanProperty).intValue());
        assertEquals(-2003567, converter.convert("-2003567", beanProperty).intValue());
        assertEquals(2000000000, converter.convert("2000000000", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithDecimal() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.DECIMAL));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(567000, converter.convert("567000", beanProperty).intValue());
        assertEquals(-2003567, converter.convert("-2003567", beanProperty).intValue());
        assertEquals(2000000000, converter.convert("2000000000", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithBinary() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.BINARY));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(1, converter.convert("1", beanProperty).intValue());
        assertEquals(2, converter.convert("10", beanProperty).intValue());
        assertEquals(3, converter.convert("11", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithOctal() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.OCTAL));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(1, converter.convert("1", beanProperty).intValue());
        assertEquals(7, converter.convert("7", beanProperty).intValue());
        assertEquals(8, converter.convert("10", beanProperty).intValue());
        assertEquals(9, converter.convert("11", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithHex() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.HEX));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(10, converter.convert("a", beanProperty).intValue());
        assertEquals(15, converter.convert("f", beanProperty).intValue());
        assertEquals(16, converter.convert("10", beanProperty).intValue());
        assertEquals(255, converter.convert("FF", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithHexOnClass() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockNumberRadixAnnotation(NumberFormat.HEX));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(10, converter.convert("a", beanProperty).intValue());
        assertEquals(15, converter.convert("f", beanProperty).intValue());
        assertEquals(16, converter.convert("10", beanProperty).intValue());
        assertEquals(255, converter.convert("FF", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithHexOverridingClassAnnotation() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockNumberRadixAnnotation(NumberFormat.BINARY));
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.HEX));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(10, converter.convert("a", beanProperty).intValue());
        assertEquals(15, converter.convert("f", beanProperty).intValue());
        assertEquals(16, converter.convert("10", beanProperty).intValue());
        assertEquals(255, converter.convert("FF", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testIntegerWithNoFormatOverridingClassAnnotation() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockNumberRadixAnnotation(NumberFormat.BINARY));
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(null));

        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        assertEquals(0, converter.convert("0", beanProperty).intValue());
        assertEquals(10, converter.convert("10", beanProperty).intValue());
        assertEquals(15, converter.convert("15", beanProperty).intValue());
        assertEquals(16, converter.convert("16", beanProperty).intValue());
        assertEquals(255, converter.convert("255", beanProperty).intValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testIntegerInvalid() throws Exception {
        NumberConverter<Integer> converter = NumberConverter.newConverter(Integer.class);
        converter.convert("123.123412", beanProperty);
    }

    @Test
    public void testLong() throws Exception {
        NumberConverter<Long> converter = NumberConverter.newConverter(Long.class);
        assertEquals(0, converter.convert("0", beanProperty).longValue());
        assertEquals(567000, converter.convert("567000", beanProperty).longValue());
        assertEquals(-2003567, converter.convert("-2003567", beanProperty).longValue());
        assertEquals(2000000000000000000L, converter.convert("2000000000000000000", beanProperty).longValue());
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testLongInvalid() throws Exception {
        NumberConverter<Long> converter = NumberConverter.newConverter(Long.class);
        converter.convert("FFFF", beanProperty);
    }

    @Test
    public void testFloat() throws Exception {
        NumberConverter<Float> converter = NumberConverter.newConverter(Float.class);
        assertEquals(0f, converter.convert("0", beanProperty), 0);
        assertEquals(123.456f, converter.convert("123.456", beanProperty), 0);
        assertEquals(-256.99f, converter.convert("-256.99", beanProperty), 0);
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testFloatInvalid() throws Exception {
        NumberConverter<Float> converter = NumberConverter.newConverter(Float.class);
        converter.convert("123.456.789", beanProperty);
    }

    @Test
    public void testDouble() throws Exception {
        NumberConverter<Double> converter = NumberConverter.newConverter(Double.class);
        assertEquals(0, converter.convert("0", beanProperty), 0);
        assertEquals(55, converter.convert("55", beanProperty), 0);
        assertEquals(123.456, converter.convert("123.456", beanProperty), 0);
        assertEquals(-256.99, converter.convert("-256.99", beanProperty), 0);
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testDoubleNumberFormatHasNoAffect() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockNumberRadixAnnotation(NumberFormat.BINARY));

        NumberConverter<Double> converter = NumberConverter.newConverter(Double.class);
        assertEquals(0, converter.convert("0", beanProperty), 0);
        assertEquals(55, converter.convert("55", beanProperty), 0);
        assertEquals(123.456, converter.convert("123.456", beanProperty), 0);
        assertEquals(-256.99, converter.convert("-256.99", beanProperty), 0);
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testDoubleInvalid() throws Exception {
        NumberConverter<Double> converter = NumberConverter.newConverter(Double.class);
        converter.convert("123.456.789", beanProperty);
    }

}
