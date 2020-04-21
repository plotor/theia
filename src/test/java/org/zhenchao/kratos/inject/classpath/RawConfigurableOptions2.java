package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;

@Configurable(Constants.CP_PREFIX + "configurable_options")
public class RawConfigurableOptions2 extends AbstractOptions {

    private static final long serialVersionUID = -4307422106479700586L;

    @Attribute(raw = true)
    private String value;

    public String getValue() {
        return value;
    }

}