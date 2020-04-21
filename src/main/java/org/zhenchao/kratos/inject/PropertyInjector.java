package org.zhenchao.kratos.inject;

import org.zhenchao.kratos.error.ConfigException;

import java.util.Properties;

/**
 * Bean property injector.
 *
 * @author zhenchao.wang 2017-09-05 17:38:43
 * @version 1.0.0
 */
public interface PropertyInjector {

    /**
     * Get bean property info.
     *
     * @return
     */
    BeanProperty getBeanProperty();

    /**
     * Get the related {@link Properties}'s name of the bean property.
     *
     * @return Returns the property key.
     */
    String getPropertyName();

    /**
     * Inject the property value by {@code properties}
     *
     * @param properties
     * @throws ConfigException
     */
    void injectValue(final Properties properties) throws ConfigException;

}
