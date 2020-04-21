package org.zhenchao.kratos.listener;

import org.zhenchao.kratos.Options;

import java.util.EventListener;

/**
 * Inject configuration listener.
 *
 * @author zhenchao.wang 2017-09-07 16:30:32
 * @version 1.0.0
 */
public interface InjectEventListener extends EventListener {

    /**
     * This method will be invoked before injection.
     *
     * @param options The options bean that will be injected.
     */
    void prevHandle(final Options options);

    /**
     * This method will be invoked after injection.
     *
     * @param options The options bean that has been injected.
     */
    void postHandle(final Options options);

}
