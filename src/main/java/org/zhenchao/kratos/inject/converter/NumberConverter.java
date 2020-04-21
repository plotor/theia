package org.zhenchao.kratos.inject.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.NumberRadix;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author zhenchao.wang 2017-09-06 10:10:45
 * @version 1.0.0
 */
public class NumberConverter<T extends Number> implements Converter<T> {

    private final Class<T> supportedClass;

    private NumberConverter(Class<T> type) {
        supportedClass = type;
    }

    public static <T extends Number> NumberConverter<T> newConverter(Class<T> type) {
        return new NumberConverter<>(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convert(String value, BeanProperty property) throws ConvertException {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            Method parse = this.resolveParseMethod();
            if (parse.getParameterTypes().length == 1) {
                return (T) parse.invoke(null, value.trim());
            }
            return (T) parse.invoke(null, value.trim(), this.getRadix(property));
        } catch (Exception e) {
            throw new ConvertException(String.format("can't parse %s as a %s", value, this.supportedClass()), e);
        }
    }

    @Override
    public Class<T> supportedClass() {
        return supportedClass;
    }

    private Method resolveParseMethod() throws NoSuchMethodException {
        String methodName = "parse" + this.supportedClass().getSimpleName();
        if (this.supportedClass() == Integer.class) {
            methodName = "parseInt";
        }
        if (this.supportedClass() == Float.class || this.supportedClass() == Double.class) {
            return this.supportedClass().getMethod(methodName, String.class);
        }
        return this.supportedClass().getMethod(methodName, String.class, int.class);
    }

    private int getRadix(BeanProperty property) {
        NumberRadix numberRadix = this.getNumberRadix(property);
        return numberRadix != null ? numberRadix.value().radix() : 10;
    }

    private NumberRadix getNumberRadix(BeanProperty property) {
        NumberRadix numberRadix = this.getNumberRadix(property.getPropAnnotations());
        return numberRadix != null ? numberRadix : this.getNumberRadix(property.getBeanAnnotations());
    }

    private NumberRadix getNumberRadix(Collection<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof NumberRadix) {
                return (NumberRadix) annotation;
            }
        }
        return null;
    }

}
