package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

import java.util.Properties;

@Configurable(resource = "GConf:PASSPORT.TEST:properties")
public class PropertiesOptions extends AbstractOptions {

    private static final long serialVersionUID = -7951631564862568043L;

    @Attribute(raw = true)
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}