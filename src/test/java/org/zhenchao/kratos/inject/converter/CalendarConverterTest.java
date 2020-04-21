package org.zhenchao.kratos.inject.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.when;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.error.ConvertException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarConverterTest extends ConverterTestSupport {

    private Converter<Calendar> converter = registry.getBuiltInConverter(Calendar.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setupBeanProperty() {
        super.setupBeanProperty();
        when(beanProperty.getName()).thenReturn("myCalendarProperty");
        when(beanProperty.getBeanType()).thenReturn((Class) Object.class);
    }

    @Test(expected = ConvertException.class)
    public void testMissingCalendarFormat() throws Exception {
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testNullCalendarFormat() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat(null));
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testInvalidCalendarFormat() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("invalid date format"));
        converter.convert("2020", beanProperty);
    }

    @Test(expected = ConvertException.class)
    public void testInvalidCalendar() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        converter.convert("invalid date", beanProperty);
    }

    @Test
    public void testConvert() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        assertEquals(this.createCalendar("2020", "yyyy"), converter.convert("2020", beanProperty));
        assertNull(converter.convert(" ", beanProperty));
        assertNull(converter.convert("", beanProperty));
        assertNull(converter.convert(null, beanProperty));
    }

    @Test
    public void testConvertAnnotationAtField() throws Exception {
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockDateFormat("yyyy"));
        assertEquals(this.createCalendar("2020", "yyyy"), converter.convert("2020", beanProperty));
    }

    @Test
    public void testConvertAnnotationAtFieldOverridesBeanAnnotation() throws Exception {
        this.setBeanAnnotations(this.mockAnnotation(Configurable.class), this.mockDateFormat("yyyy"));
        this.setPropAnnotations(this.mockAnnotation(Attribute.class), this.mockDateFormat("yyyyMMdd"));

        assertEquals(this.createCalendar("20200115", "yyyyMMdd"), converter.convert("20200115", beanProperty));
    }

    private Calendar createCalendar(String date, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat(format).parse(date));
            return cal;
        } catch (Exception e) {
            return null;
        }
    }

}
