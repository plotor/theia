package org.zhenchao.kratos.inject.converter;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;

/**
 * @author zhenchao.wang 2017-09-06 10:06:15
 * @version 1.0.0
 */
public class BooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean convert(String value, BeanProperty property) throws ConvertException {
        return StringUtils.isBlank(value) ? null : this.parse(value.trim());
    }

    @Override
    public Class<Boolean> supportedClass() {
        return Boolean.class;
    }

    /**
     * Parse {@link String} to {@link Boolean}.
     *
     * @param value
     * @return
     * @throws ConvertException
     */
    private boolean parse(String value) throws ConvertException {
        boolean bool = Boolean.parseBoolean(value)
                || value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("t")
                || value.equals("1");

        if (!bool) {
            if (this.notValid(value)) {
                throw new ConvertException("can't parse the value to boolean type: " + value);
            }
        }
        return bool;
    }

    private boolean notValid(String value) {
        return !(value.equalsIgnoreCase("false")
                || value.equalsIgnoreCase("f")
                || value.equalsIgnoreCase("no")
                || value.equalsIgnoreCase("n")
                || value.equals("0"));
    }
}
