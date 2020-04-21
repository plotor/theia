package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.zhenchao.kratos.error.ConvertException;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GenericConverterTest extends ConverterTestSupport {

    private Converter<Object> converter = registry.getBuiltInConverter(Object.class);

    @Test
    public void testConvert1() throws Exception {
        this.setPropertyType(File.class);
        assertEquals(new File("/tmp/test.txt"), converter.convert("/tmp/test.txt", beanProperty));
    }

    @Test
    public void testConvert2() throws Exception {
        this.setPropertyType(SimpleDateFormat.class);
        assertEquals(new SimpleDateFormat("yyyy"), converter.convert("yyyy", beanProperty));
    }

    @Test
    public void testConvert3() throws Exception {
        this.setPropertyType(BigInteger.class);
        assertEquals(new BigInteger("1000"), converter.convert("1000", beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testConvertWithBadValue() throws Exception {
        this.setPropertyType(BigInteger.class);
        converter.convert("", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testConvertWithInvalidType() throws Exception {
        this.setPropertyType(Calendar.class);
        converter.convert("2020", beanProperty);
    }

}
