package org.zhenchao.kratos.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;
import org.zhenchao.kratos.inject.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhenchao.wang 2020-01-16 15:24
 * @version 1.0.0
 */
public class ListConverter implements Converter<List<String>> {

    @Override
    public List<String> convert(String value, BeanProperty property) throws ConvertException {
        if (StringUtils.isBlank(value)) {
            return new ArrayList<>();
        }
        return Arrays.stream(value.split("\\s+")).collect(Collectors.toList());
    }

    @Override
    public Class<?> supportedClass() {
        return List.class;
    }
}
