package org.zhenchao.kratos.inject.zk;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

@Configurable(resource = "ZK:/services/com.xiaomi.passport/passport-test/text")
public class TextOptions extends AbstractOptions {

    private static final long serialVersionUID = 2582750227297678555L;

    @Attribute(raw = true)
    private String text;

    public String getText() {
        return text;
    }
}