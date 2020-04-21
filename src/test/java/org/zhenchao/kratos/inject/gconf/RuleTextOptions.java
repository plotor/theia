package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

/**
 * @author zhenchao.wang 2018-09-19 10:19
 * @version 1.0.0
 */
@Configurable(resource = "GConf:PASSPORT.TEST:drl")
public class RuleTextOptions extends AbstractOptions {

    private static final long serialVersionUID = 8507594891022778329L;

    @Attribute(raw = true)
    private String drlText;

    public String getDrlText() {
        return drlText;
    }

}
