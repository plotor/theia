package org.zhenchao.kratos.inject.gconf;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;

@Configurable("GConf:PASSPORT.TEST:basic")
public class BasicOptions extends AbstractOptions {

    private static final long serialVersionUID = 1165395286153841276L;

    @Attribute(name = "name")
    private String name;

    @Override
    public void update() {
        System.out.println("update name = " + name);
    }

    public String getName() {
        return this.name;
    }

}