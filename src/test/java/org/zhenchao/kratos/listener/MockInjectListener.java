package org.zhenchao.kratos.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.Options;

/**
 * @author zhenchao.wang 2020-01-16 16:20
 * @version 1.0.0
 */
public class MockInjectListener implements InjectEventListener {

    private static final Logger log = LoggerFactory.getLogger(MockInjectListener.class);

    @Override
    public void prevHandle(Options options) {
        log.debug("Starting Configuration: " + options);
    }

    @Override
    public void postHandle(Options options) {
        log.debug("Completed Configuration: " + options);
    }
}