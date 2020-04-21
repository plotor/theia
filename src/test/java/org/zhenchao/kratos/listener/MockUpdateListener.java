package org.zhenchao.kratos.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.Options;

/**
 * @author zhenchao.wang 2020-01-16 16:18
 * @version 1.0.0
 */
public class MockUpdateListener implements UpdateEventListener {

    private static final Logger log = LoggerFactory.getLogger(MockUpdateListener.class);

    private Options lastOptions;
    private int count = 0;

    @Override
    public void prevHandle(Options options) {
        log.debug("Starting Bean Update: " + options);
    }

    @Override
    public void postHandle(Options options) {
        log.debug("Completed Bean Update: " + options);
        this.lastOptions = options;
        this.count++;
    }

    public void setLastOptions(Options lastOptions) {
        this.lastOptions = lastOptions;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Options getLastOptions() {
        return lastOptions;
    }

    public int getCount() {
        return count;
    }
}