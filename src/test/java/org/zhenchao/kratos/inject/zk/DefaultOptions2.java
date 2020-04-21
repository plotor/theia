package org.zhenchao.kratos.inject.zk;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhenchao.wang 2018-01-30 10:29
 * @version 1.0.0
 */
@Configurable(resource = "ZK:/services/com.xiaomi.passport/passport-test/default")
public class DefaultOptions2 extends AbstractOptions {

    private static final long serialVersionUID = 6028832012294963740L;

    @Attribute(name = "rule.names")
    private String[] ruleNames;

    private List<String> ruleNameList = new ArrayList<>();

    @Override
    public void update() {
        ruleNameList = new ArrayList<>(Arrays.asList(ruleNames));
    }

    public List<String> getRuleNameList() {
        return ruleNameList;
    }
}
