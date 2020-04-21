package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

@Configurable("CLASSPATH:environment_options")
public class EnvironmentOptions extends AbstractOptions {

    private static final long serialVersionUID = -8397483334130219344L;

    @Attribute("fake-env-prop")
    private String envValue;

    @Attribute("fake-env-prop-2")
    private String envValue2;

    @Attribute(name = "fake.system.property")
    private String sysValue;

    @Attribute("cus.value")
    private String cusValue;

    public String getEnvValue() {
        return this.envValue;
    }

    public String getEnvValue2() {
        return envValue2;
    }

    public String getSysValue() {
        return this.sysValue;
    }

    public String getCusValue() {
        return cusValue;
    }
}