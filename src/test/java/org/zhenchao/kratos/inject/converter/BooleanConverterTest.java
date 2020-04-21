package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.zhenchao.kratos.error.ConvertException;

public class BooleanConverterTest extends ConverterTestSupport {

    private Converter<Boolean> converter = registry.getBuiltInConverter(Boolean.class);

    @Test
    public void testConvert() throws Exception {
        assertTrue(converter.convert("true", beanProperty));
        assertTrue(converter.convert("True", beanProperty));
        assertTrue(converter.convert("TRUE", beanProperty));
        assertTrue(converter.convert("T", beanProperty));
        assertTrue(converter.convert("t", beanProperty));
        assertTrue(converter.convert("yes", beanProperty));
        assertTrue(converter.convert("YES", beanProperty));
        assertTrue(converter.convert("Y", beanProperty));
        assertTrue(converter.convert("1", beanProperty));
        assertFalse(converter.convert("false", beanProperty));
        assertFalse(converter.convert("False", beanProperty));
        assertFalse(converter.convert("FALSE", beanProperty));
        assertFalse(converter.convert("F", beanProperty));
        assertFalse(converter.convert("f", beanProperty));
        assertFalse(converter.convert("no", beanProperty));
        assertFalse(converter.convert("NO", beanProperty));
        assertFalse(converter.convert("n", beanProperty));
        assertFalse(converter.convert("0", beanProperty));
    }

    @Test
    public void testConvertWithBlankOrNull() throws Exception {
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test(expected = ConvertException.class)
    public void testConvertWithBadValue() throws Exception {
        converter.convert("not a boolean", beanProperty);
    }

}
