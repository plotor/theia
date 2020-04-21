package org.zhenchao.kratos.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;
import org.zhenchao.kratos.inject.converter.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhenchao.wang 2020-01-16 15:27
 * @version 1.0.0
 */
public class SetConverter implements Converter<Set<String>> {

    @Override
    public Set<String> convert(String value, BeanProperty property) throws ConvertException {
        if (StringUtils.isBlank(value)) {
            return new HashSet<>();
        }
        return Arrays.stream(value.split(",\\s*")).collect(Collectors.toSet());
    }

    @Override
    public Class<?> supportedClass() {
        return Set.class;
    }
}
