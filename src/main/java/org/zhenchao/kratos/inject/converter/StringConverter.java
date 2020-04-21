package org.zhenchao.kratos.inject.converter;

import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

/**
 * @author zhenchao.wang 2017-09-06 10:10:40
 * @version 1.0.0
 */
public class StringConverter implements Converter<String> {

    @Override
    public String convert(String value, BeanProperty property) throws ConvertException {
        return value;
    }

    @Override
    public Class<String> supportedClass() {
        return String.class;
    }

}
