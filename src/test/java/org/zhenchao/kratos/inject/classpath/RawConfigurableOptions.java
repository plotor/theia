package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;

import java.util.Properties;

@Configurable(Constants.CP_PREFIX + "configurable_options")
public class RawConfigurableOptions extends AbstractOptions {

    private static final long serialVersionUID = -3267431599020193977L;

    @Attribute(raw = true)
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

}