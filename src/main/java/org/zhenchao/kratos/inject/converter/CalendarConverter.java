package org.zhenchao.kratos.inject.converter;

import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zhenchao.wang 2017-09-07 10:06:15
 * @version 1.0.0
 */
public class CalendarConverter implements Converter<Calendar> {

    @Override
    public Calendar convert(String value, BeanProperty property) throws ConvertException {
        Calendar calendar = null;
        DateConverter converter = new DateConverter();
        Date date = converter.convert(value, property);
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        return calendar;
    }

    @Override
    public Class<Calendar> supportedClass() {
        return Calendar.class;
    }

}
