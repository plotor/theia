package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArrayConverterTest extends ConverterTestSupport {

    @Test
    public void testSplit() {
        ArrayConverter<String[]> converter = ArrayConverter.newConverter(String[].class);
        String[] res = converter.split("this, \"\"is\"\", \"a test\", \"of, my,delimitedlist\", oh yes, ");
        assertEquals(6, res.length);
        assertArrayEquals(new String[] {"this", "\"is\"", "a test", "of, my,delimitedlist", "oh yes", ""}, res);
    }

    @Test
    public void testBoolean() throws Exception {
        ArrayConverter<boolean[]> converter = ArrayConverter.newConverter(boolean[].class);
        boolean[] res = converter.convert("true, false, 0, 1, yes", beanProperty);
        assertEquals(5, res.length);
        this.assertBooleanArrayEquals(new boolean[] {true, false, false, true, true}, res);
    }

    @Test
    public void testBooleanWithEmptyValue() throws Exception {
        ArrayConverter<boolean[]> converter = ArrayConverter.newConverter(boolean[].class);
        boolean[] res = converter.convert("true, false, , 1, yes", beanProperty);
        assertEquals(5, res.length);
        this.assertBooleanArrayEquals(new boolean[] {true, false, false, true, true}, res);
    }

    @Test
    public void testCharWithEmptyValue() throws Exception {
        ArrayConverter<char[]> converter = ArrayConverter.newConverter(char[].class);
        char[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new char[] {'0', '1', '\u0000', '3', '4'}, res);
    }

    @Test
    public void testFloatWithEmptyValue() throws Exception {
        ArrayConverter<float[]> converter = ArrayConverter.newConverter(float[].class);
        float[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        this.assertFloatArrayEquals(new float[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testDoubleWithEmptyValue() throws Exception {
        ArrayConverter<double[]> converter = ArrayConverter.newConverter(double[].class);
        double[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        this.assertDoubleArrayEquals(new double[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testByteWithEmptyValue() throws Exception {
        ArrayConverter<byte[]> converter = ArrayConverter.newConverter(byte[].class);
        byte[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new byte[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testShortWithEmptyValue() throws Exception {
        ArrayConverter<short[]> converter = ArrayConverter.newConverter(short[].class);
        short[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new short[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testIntWithEmptyValue() throws Exception {
        ArrayConverter<int[]> converter = ArrayConverter.newConverter(int[].class);
        int[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new int[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testLongWithEmptyValue() throws Exception {
        ArrayConverter<long[]> converter = ArrayConverter.newConverter(long[].class);
        long[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new long[] {0, 1, 0, 3, 4}, res);
    }

    @Test
    public void testStringWithEmptyValue() throws Exception {
        ArrayConverter<String[]> converter = ArrayConverter.newConverter(String[].class);
        String[] res = converter.convert("0, 1, , 3, 4", beanProperty);
        assertEquals(5, res.length);
        assertArrayEquals(new String[] {"0", "1", "", "3", "4"}, res);
    }

    @Test
    public void testDateWithEmptyValue() throws Exception, ParseException {
        this.setPropAnnotations(this.mockDateFormat("yyyy"));

        ArrayConverter<Date[]> converter = ArrayConverter.newConverter(Date[].class);
        Date[] res = converter.convert("2001, 2002, , 2004, 2005", beanProperty);

        assertEquals(5, res.length);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        assertArrayEquals(new Date[] {sdf.parse("2001"), sdf.parse("2002"), null, sdf.parse("2004"), sdf.parse("2005")}, res);
    }

    private void assertBooleanArrayEquals(boolean[] expected, boolean[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private void assertFloatArrayEquals(float[] expected, float[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0);
        }
    }

    private void assertDoubleArrayEquals(double[] expected, double[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0);
        }
    }

}
