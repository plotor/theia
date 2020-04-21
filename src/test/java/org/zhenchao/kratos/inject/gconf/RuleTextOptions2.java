package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

import java.util.Properties;

/**
 * @author zhenchao.wang 2018-09-19 10:19
 * @version 1.0.0
 */
@Configurable(resource = "GConf:PASSPORT.TEST:drl")
public class RuleTextOptions2 extends AbstractOptions {

    private static final long serialVersionUID = 6656957179957259012L;

    @Attribute(raw = true)
    private Properties drlText;

    public Properties getDrlText() {
        return drlText;
    }

}
