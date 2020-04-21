package org.zhenchao.kratos.listener;

import org.zhenchao.kratos.Options;

import java.util.EventListener;

/**
 * Update configuration listener.
 *
 * @author zhenchao.wang 2017-09-07 16:31:13
 * @version 1.0.0
 */
public interface UpdateEventListener extends EventListener {

    /**
     * This method will be invoked before update.
     *
     * @param options The options bean that will be updated.
     */
    void prevHandle(Options options);

    /**
     * This method will be invoked after update.
     *
     * @param options The options bean that has been updated.
     */
    void postHandle(Options options);

}
