package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

/**
 * @author zhenchao.wang 2018-04-23 14:42
 * @version 1.0.0
 */
@Configurable(resource = "GConf:PASSPORT.TEST:raw_content")
public class RawStringOptions extends AbstractOptions {

    private static final long serialVersionUID = -908900574927664246L;

    @Attribute(raw = true)
    private String value;

    public String getValue() {
        return value;
    }
}
