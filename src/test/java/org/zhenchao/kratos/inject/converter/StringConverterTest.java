package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

public class StringConverterTest extends ConverterTestSupport {

    private Converter<String> converter = registry.getBuiltInConverter(String.class);

    @Test
    public void testConvert() throws Exception {
        // the string property converter is just a pass through.
        assertSame("this is a string", converter.convert("this is a string", beanProperty));
        assertSame("", converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

}
