package org.zhenchao.kratos.inject.converter;

import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zhenchao.wang 2017-09-06 10:10:50
 * @version 1.0.0
 */
public class GenericConverter implements Converter<Object> {

    @Override
    public Object convert(String value, BeanProperty property) throws ConvertException {
        final Class<?> propType = property.getType();
        try {
            Constructor<?> constructor = propType.getConstructor(String.class);
            return constructor.newInstance(value);
        } catch (InstantiationException
                | InvocationTargetException
                | NoSuchMethodException
                | IllegalAccessException e) {
            throw new ConvertException("can't create instance for class: " + propType, e);
        }
    }

    @Override
    public Class<?> supportedClass() {
        return Object.class;
    }

}
