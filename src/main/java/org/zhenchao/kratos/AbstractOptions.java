package org.zhenchao.kratos;

/**
 * @author zhenchao.wang 2020-01-14 13:24
 * @version 1.0.0
 */
public abstract class AbstractOptions implements Options {

    private static final long serialVersionUID = 225332297232914090L;

    @Override
    public void update() {

    }

    @Override
    public boolean validate() {
        return true;
    }

}
