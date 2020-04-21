package org.zhenchao.kratos.source.provider;

import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.source.PropertiesBuilder;
import org.zhenchao.kratos.source.Source;

import java.util.Properties;

/**
 * @author zhenchao.wang 2016-09-06 11:46:12
 * @version 1.0.0
 */
public interface SourceProvider extends Comparable<SourceProvider> {

    /**
     * Invoke in initialize.
     */
    void onInitialize();

    /**
     * Load configuration from source, and convert to properties by {@link PropertiesBuilder}.
     *
     * @param source
     * @param builder
     * @return
     * @throws ConfigException
     */
    Properties loadProperties(final Source source, final PropertiesBuilder builder) throws ConfigException;

    /**
     * Check if the source is supported by current provider.
     *
     * @param source
     * @return
     */
    boolean support(final Source source);

    /**
     * The priority of the source, the higher value mean the higher priority.
     *
     * @return
     */
    int priority();

    /**
     * Invoke on destroy.
     */
    void onDestroy();

}
