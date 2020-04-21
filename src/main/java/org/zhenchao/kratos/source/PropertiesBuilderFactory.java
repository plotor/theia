package org.zhenchao.kratos.source;

/**
 * {@link PropertiesBuilder} factory.
 *
 * @author zhenchao.wang 2016-09-06 13:59:33
 * @version 1.0.0
 */
public final class PropertiesBuilderFactory {

    private boolean addEnvProperties;
    private boolean addSysProperties;

    public PropertiesBuilderFactory() {
        this(false, false);
    }

    public PropertiesBuilderFactory(boolean addEnvProperties, boolean addSysProperties) {
        this.addEnvProperties = addEnvProperties;
        this.addSysProperties = addSysProperties;
    }

    public PropertiesBuilder newPropertiesBuilder() {
        PropertiesBuilder builder = new PropertiesBuilder();
        if (this.isAddEnvProperties()) {
            builder.addAllEnvironmentProperties();
        }
        if (this.isAddSysProperties()) {
            builder.addAllSystemProperties();
        }
        return builder;
    }

    public void setAddEnvProperties(boolean addEnvProperties) {
        this.addEnvProperties = addEnvProperties;
    }

    public boolean isAddEnvProperties() {
        return this.addEnvProperties;
    }

    public void setAddSysProperties(boolean addSysProperties) {
        this.addSysProperties = addSysProperties;
    }

    public boolean isAddSysProperties() {
        return this.addSysProperties;
    }

}
