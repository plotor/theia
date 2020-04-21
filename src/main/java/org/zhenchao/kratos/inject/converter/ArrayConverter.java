package org.zhenchao.kratos.inject.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.anno.TestingVisible;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;
import org.zhenchao.kratos.util.ConfUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhenchao.wang 2016-09-07 09:07:44
 * @version 1.0.0
 */
public class ArrayConverter<T> implements Converter<T> {

    private static final Logger log = LoggerFactory.getLogger(ArrayConverter.class);

    private static final Pattern PATTERN = Pattern.compile("(?:^|,)(\\\"(?:[^\\\"]+|\\\"\\\")*\\\"|[^,]*)");

    private final Class<T> arrayType;

    private ArrayConverter(Class<T> arrayType) {
        this.arrayType = arrayType;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> ArrayConverter<T> newConverter(Class<T> arrayType) {
        return new ArrayConverter(arrayType);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public T convert(String value, final BeanProperty property) throws ConvertException {
        final Class<?> arrayComponentType = arrayType.getComponentType();
        final Converter converter = ConverterRegistry.getInstance().getBuiltInConverter(arrayComponentType);
        // do convert
        String[] splitValues = this.split(value);
        log.debug("Split value by array converter, " +
                "origin[{}], result[{}].", value, Arrays.toString(splitValues));
        T data = (T) Array.newInstance(arrayComponentType, splitValues.length);
        for (int i = 0; i < splitValues.length; i++) {
            final String splitValue = splitValues[i];
            log.debug("Convert value[{}] to {} by converter[{}].", splitValue, arrayComponentType, converter.getClass());
            Object itemVal = converter.convert(splitValue, property);
            if (null == itemVal && arrayComponentType.isPrimitive()) {
                itemVal = ConfUtils.resolveDefaultValue(arrayComponentType);
            }
            Array.set(data, i, itemVal);
        }
        return data;
    }

    @Override
    public Class<?> supportedClass() {
        return Array.newInstance(arrayType.getComponentType(), 0).getClass();
    }

    @TestingVisible
    String[] split(String originalValue) {
        List<String> list = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(originalValue);
        StringBuilder quotedGroup = new StringBuilder();
        boolean inQuotedLoop = false;
        while (matcher.find()) {
            String group = matcher.group();
            if (!inQuotedLoop) {
                if (group.startsWith(",")) {
                    group = group.substring(1);
                }
                group = group.trim();
            }

            if (group.startsWith("\"")) {
                group = group.substring(1);
                if (group.endsWith("\"")) {
                    group = group.substring(0, group.length() - 1);
                } else {
                    quotedGroup.append(group);
                    inQuotedLoop = true;
                    continue;
                }
            }

            if (inQuotedLoop) {
                if (group.endsWith("\"")) {
                    quotedGroup.append(group, 0, group.length() - 1);
                    inQuotedLoop = false;
                    group = quotedGroup.toString();
                } else {
                    quotedGroup.append(group);
                    continue;
                }
            }

            list.add(group);
        }

        return list.toArray(new String[0]);
    }

}
