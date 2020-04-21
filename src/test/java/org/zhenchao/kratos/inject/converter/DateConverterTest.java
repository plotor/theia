package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.when;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.error.ConvertException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverterTest extends ConverterTestSupport {

    private Converter<Date> converter = registry.getBuiltInConverter(Date.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setupBeanProperty() {
        super.setupBeanProperty();
        when(beanProperty.getName()).thenReturn("myDateProperty");
        when(beanProperty.getBeanType()).thenReturn((Class) Object.class);
    }

    @Test(expected = ConvertException.class)
    public void testMissingDateFormat() throws Exception {
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testNullDateFormat() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat(null));
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testInvalidDateFormat() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("this isn't a real date format"));
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testInvalidDate() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        converter.convert("invalid date", beanProperty);
    }

    @Test
    public void testConvert() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        assertEquals(this.createDate("2020", "yyyy"), converter.convert("2020", beanProperty));
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testConvertWithAnnotationAtField() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockDateFormat("yyyy"));
        assertEquals(this.createDate("2020", "yyyy"), converter.convert("2020", beanProperty));
    }

    @Test
    public void testConvertWithAnnotationAtFieldOverridesBeanAnnotation() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockDateFormat("yyyyMMdd"));
        assertEquals(this.createDate("20200115", "yyyyMMdd"), converter.convert("20200115", beanProperty));
    }

    private Date createDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

}
