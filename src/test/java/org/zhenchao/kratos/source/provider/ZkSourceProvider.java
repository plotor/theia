package org.zhenchao.kratos.source.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.zhenchao.kratos.ConfigInjector;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.source.PropertiesBuilder;
import org.zhenchao.kratos.source.Source;
import org.zhenchao.kratos.util.Bytes;
import org.zhenchao.kratos.util.ConfUtils;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author zhenchao.wang 2020-04-22 17:51
 * @version 1.0.0
 */
public class ZkSourceProvider extends AbstractSourceProvider implements SourceProvider {

    private final CuratorFramework zkClient;

    private final Set<Source> sourceRegistry = new HashSet<>();

    public ZkSourceProvider() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        this.zkClient.start();
    }

    @Override
    protected Properties doLoadProperties(final Source source, final PropertiesBuilder builder) throws ConfigException {
        final Class<?> optionsClass = source.getOptionsClass();
        final String resourceName = this.resourceName(source);
        log.info("Load zk configuration, resource[{}], options[{}].", resourceName, optionsClass);

        try {
            final String zkPath = this.toZkPath(resourceName);
            final byte[] bytes = zkClient.getData().forPath(zkPath);
            if (null == bytes || 0 == bytes.length) {
                log.warn("No zk property value resolved, path[{}].", zkPath);
                return builder.build();
            }
            final String data = Bytes.toString(bytes);
            if (StringUtils.isBlank(data)) {
                log.warn("No zk property value resolved, path[{}].", zkPath);
                return builder.build();
            }

            if (log.isDebugEnabled()) {
                log.debug("Get zk property, path[{}], value[{}].", zkPath, data);
            }

            final Properties properties = this.toProperties(data);
            if (!properties.isEmpty()) {
                builder.addAll(ConfUtils.toMap(properties));
            }
            return builder.build();
        } catch (Throwable t) {
            log.error("Load zk configuration error, resource[{}], optionsClass[{}]", resourceName, optionsClass, t);
            throw new ConfigException("load zk configuration error, " +
                    "resource: " + resourceName + ", options: " + optionsClass, t);
        }

    }

    @Override
    public void postLoad(Source source) {
        if (!this.tryRegisterListener(source)) {
            throw new IllegalStateException("register zk listener error, " +
                    "resource: " + source.getResourceName() + ", options: " + source.getOptionsClass());
        }
    }

    @Override
    protected String resourceName(Source source) {
        String resourceName = source.getResourceName();
        Validate.isTrue(ConfUtils.isZkResource(resourceName), "invalid zk resource name: " + resourceName);
        return resourceName;
    }

    @Override
    public boolean support(Source source) {
        return StringUtils.startsWithIgnoreCase(super.resourceName(source), Constants.ZK_PREFIX);
    }

    @Override
    public int priority() {
        return 0;
    }

    private String toZkPath(String resourceName) {
        return resourceName.substring(Constants.ZK_PREFIX.length()).trim();
    }

    /**
     * Register zk data change listener.
     *
     * @param source
     * @return
     */
    private boolean tryRegisterListener(final Source source) {
        if (sourceRegistry.contains(source)) {
            return true;
        }

        final String zkPath = this.toZkPath(this.resourceName(source));
        log.info("Register zk data change listener for path[{}].", zkPath);
        try {
            zkClient.getData()
                    .usingWatcher((CuratorWatcher) event -> {
                        final Watcher.Event.EventType eventType = event.getType();
                        // uninterested zk event
                        if (!Watcher.Event.EventType.NodeDataChanged.equals(eventType)) {
                            log.info("Uninterested zk event type: {}, and ignore it.", eventType);
                            return;
                        }

                        final String eventPath = event.getPath();
                        try {
                            if (zkPath.equals(eventPath)) {
                                log.info("Refresh zk configuration, path[{}].", eventPath);
                                ConfigInjector.getInstance().reload(source);
                            } else {
                                log.debug("[{}] unexpected change, and ignore it, path[{}].", zkPath, eventPath);
                            }
                        } catch (Throwable t) {
                            throw new ConfigException("refresh zk configuration error, path: " + eventPath, t);
                        }
                    })
                    .forPath(zkPath);
        } catch (Throwable t) {
            log.error("Try register zk data change listener error, path: {}", zkPath, t);
            return false;
        }
        sourceRegistry.add(source);
        return true;
    }
}
