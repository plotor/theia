package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;

import java.util.Properties;

/**
 * @author zhenchao.wang 2020-01-17 18:14
 * @version 1.0.0
 */
@Configurable(Constants.CP_PREFIX + "raw")
public class RawContentOptions2 extends AbstractOptions {

    private static final long serialVersionUID = 7027489617851759974L;

    @Attribute(raw = true)
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}
