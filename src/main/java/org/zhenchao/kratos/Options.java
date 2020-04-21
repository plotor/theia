package org.zhenchao.kratos;

import java.io.Serializable;

/**
 * @author zhenchao.wang 2020-01-10 15:17
 * @version 1.0.0
 */
public interface Options extends Serializable {

    /**
     * This method will be invoked after successfully injected.
     */
    void update();

    /**
     * Validate that the configuration is correctly.
     *
     * @return {@code true} means correctly, or {@code false}.
     */
    boolean validate();

}
