package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

import java.util.Properties;

/**
 * @author zhenchao.wang 2020-01-17 17:11
 * @version 1.0.0
 */
@Configurable(resource = "GConf:PASSPORT.TEST:raw_content")
public class RawPropertiesOptions extends AbstractOptions {

    private static final long serialVersionUID = 2753254683579522695L;

    @Attribute(raw = true)
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}
