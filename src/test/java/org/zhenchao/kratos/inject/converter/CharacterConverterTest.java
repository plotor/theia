package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CharacterConverterTest extends ConverterTestSupport {

    private Converter<Character> converter = registry.getBuiltInConverter(Character.class);

    @Test
    public void testConvert() throws Exception {
        assertEquals(' ', converter.convert(" ", beanProperty).charValue());
        assertEquals('a', converter.convert("a", beanProperty).charValue());
        assertEquals('B', converter.convert("B", beanProperty).charValue());
        assertEquals('$', converter.convert("$", beanProperty).charValue());
    }

    @Test
    public void testConvertWithEmptyOrNull() throws Exception {
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testConvertWithFullString() throws Exception {
        // will still convert strings, but just uses the first character
        assertEquals('l', converter.convert("longer string", beanProperty).charValue());
        assertEquals(' ', converter.convert("  untrimmed string  ", beanProperty).charValue());
    }

}
