package org.zhenchao.kratos.inject.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.DatePattern;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author zhenchao.wang 2017-09-06 10:09:05
 * @version 1.0.0
 */
public class DateConverter implements Converter<Date> {

    @Override
    public Date convert(String value, BeanProperty property) throws ConvertException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        final SimpleDateFormat dateFormat = this.createDateFormat(property);
        if (dateFormat == null) {
            throw new ConvertException(
                    String.format("field '%s' or class '%s' must be annotated by @%s",
                            property.getName(), property.getBeanType(), DateFormat.class.getSimpleName()));
        }
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new ConvertException(
                    String.format("can't format property %s of %s, value %s did not fit provided date format %s",
                            property.getName(), property.getBeanType(), value, dateFormat.toPattern()));
        }
    }

    @Override
    public Class<?> supportedClass() {
        return Date.class;
    }

    private SimpleDateFormat createDateFormat(BeanProperty property) throws ConvertException {
        DatePattern format = null;
        try {
            format = this.findDateFormat(property.getPropAnnotations());
            if (format == null) {
                format = this.findDateFormat(property.getBeanAnnotations());
            }
            if (format != null) {
                return new SimpleDateFormat(format.value());
            }
        } catch (NullPointerException e) {
            throw new ConvertException("requires a date format to be specified for property: "
                    + property.getBeanType() + "." + property.getName());
        } catch (IllegalArgumentException e) {
            throw new ConvertException(
                    String.format("%s is not a valid format for property %s.%s",
                            format == null ? null : format.value(), property.getBeanType(), property.getName()));
        }
        return null;
    }

    private DatePattern findDateFormat(Collection<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof DatePattern) {
                return (DatePattern) annotation;
            }
        }
        return null;
    }

}
