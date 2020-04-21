package org.zhenchao.kratos.inject.converter;

import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

/**
 * @author zhenchao.wang 2016-09-07 09:19:22
 * @version 1.0.0
 */
public interface Converter<T> {

    /**
     * Convert {@link String} value to target type.
     *
     * @param value
     * @param property
     * @return
     * @throws ConvertException
     */
    T convert(String value, final BeanProperty property) throws ConvertException;

    /**
     * The supported class of the current {@link Converter}.
     *
     * @return
     */
    Class<?> supportedClass();

}
