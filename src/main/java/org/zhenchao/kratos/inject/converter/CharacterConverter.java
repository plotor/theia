package org.zhenchao.kratos.inject.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

/**
 * @author zhenchao.wang 2017-09-06 10:08:21
 * @version 1.0.0
 */
public class CharacterConverter implements Converter<Character> {

    @Override
    public Character convert(String value, BeanProperty property) throws ConvertException {
        // just return the first character
        return StringUtils.isEmpty(value) ? null : value.charAt(0);
    }

    @Override
    public Class<Character> supportedClass() {
        return Character.class;
    }

}
