package org.zhenchao.kratos.source;

import java.util.Map;

/**
 * @author zhenchao.wang 2017-09-06 13:35:35
 * @version 1.0.0
 */
public interface Environment {

    /**
     * Get all env properties.
     *
     * @return
     */
    Map<String, String> getEnvironmentProperties();

    /**
     * Get all sys properties.
     *
     * @return
     */
    Map<String, String> getSystemProperties();

    /**
     * Get env property by name.
     *
     * @param name
     * @return {@code null} if not exist.
     */
    String getEnvironmentProperty(String name);

    /**
     * Get env property by name.
     *
     * @param name
     * @param defaultValue
     * @return {@code null} if not exist.
     */
    String getEnvironmentProperty(String name, String defaultValue);

    /**
     * Get sys property by name.
     *
     * @param name
     * @return {@code null} if not exist.
     */
    String getSystemProperty(String name);

    /**
     * Get sys property by name.
     *
     * @param name
     * @param defaultValue
     * @return {@code null} if not exist.
     */
    String getSystemProperty(String name, String defaultValue);

    /**
     * Check if contains system property.
     *
     * @param name
     * @return
     */
    boolean containsSystemProperty(String name);

    /**
     * Check if contains environment property.
     *
     * @param name
     * @return
     */
    boolean containsEnvironmentProperty(String name);

}
