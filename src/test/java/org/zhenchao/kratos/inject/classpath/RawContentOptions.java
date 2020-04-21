package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;

/**
 * @author zhenchao.wang 2020-01-17 18:14
 * @version 1.0.0
 */
@Configurable(Constants.CP_PREFIX + "raw")
public class RawContentOptions extends AbstractOptions {

    private static final long serialVersionUID = 4106573134713760425L;

    @Attribute(raw = true)
    private String value;

    public String getValue() {
        return value;
    }
}
