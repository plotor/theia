package org.zhenchao.kratos.inject.converter;

import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

/**
 * @author zhenchao.wang 2020-01-14 10:20
 * @version 1.0.0
 */
public class VoidConverter implements Converter<Void> {

    @Override
    public Void convert(String value, BeanProperty property) throws ConvertException {
        return null;
    }

    @Override
    public Class<?> supportedClass() {
        return Void.class;
    }

}
