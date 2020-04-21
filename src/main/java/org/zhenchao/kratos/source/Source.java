package org.zhenchao.kratos.source;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.Options;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.util.ConfUtils;

import java.util.Objects;

/**
 * Data source.
 *
 * @author zhenchao.wang 2016-12-01 14:58:03
 * @version 1.0.0
 */
public final class Source {

    private final Class<? extends Options> optionsClass;
    private final String resourceName;
    private final boolean autoload;

    public Source(final Options options) {
        Validate.notNull(options, "null options");
        Class<? extends Options> optionsClass = options.getClass();

        Configurable configurable = optionsClass.getAnnotation(Configurable.class);
        if (null == configurable) {
            throw new IllegalStateException(
                    "missing @" + Configurable.class.getSimpleName() + " annotation, class: " + optionsClass);
        }

        final String resourceName = StringUtils.defaultIfBlank(configurable.resource(), configurable.value());
        this.verification(optionsClass, resourceName);

        this.optionsClass = optionsClass;
        this.resourceName = resourceName;
        this.autoload = configurable.autoload();
    }

    public Source(Class<? extends Options> optionsClass, String resourceName) {
        this(optionsClass, resourceName, false);
    }

    public Source(Class<? extends Options> optionsClass, String resourceName, boolean autoload) {
        this.verification(optionsClass, resourceName);
        this.optionsClass = optionsClass;
        this.resourceName = resourceName;
        this.autoload = autoload;
    }

    public Class<? extends Options> getOptionsClass() {
        return this.optionsClass;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public boolean isAutoload() {
        return autoload;
    }

    @Override
    public final int hashCode() {
        int res = 31;
        if (null != optionsClass) {
            res += optionsClass.hashCode();
        }
        if (null != resourceName) {
            res += resourceName.hashCode();
        }
        return res + (autoload ? 1 : 0);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof Source)) {
            return false;
        }

        Source that = (Source) obj;
        return Objects.equals(optionsClass, that.optionsClass)
                && StringUtils.equals(resourceName, that.resourceName)
                && autoload == that.autoload;
    }

    @Override
    public String toString() {
        return "options: " + optionsClass.getSimpleName() + ", resource: " + resourceName + ", autoload: " + autoload;
    }

    private void verification(Class<? extends Options> beanClass, String resourceName) {
        if (null == beanClass || !beanClass.isAnnotationPresent(Configurable.class)) {
            throw new IllegalStateException(
                    "missing @" + Configurable.class.getSimpleName() + ", class: " + beanClass);
        }

        if (StringUtils.isBlank(resourceName)) {
            throw new IllegalStateException("invalid resource: " + resourceName + ", class: " + beanClass);
        }

        final String[] elems = resourceName.trim().split(Constants.DELIMITER);
        if (StringUtils.isBlank(resourceName) || ConfUtils.notValidPrefix(elems[0])) {
            throw new IllegalStateException("invalid resource: " + resourceName + ", class: " + beanClass);
        }
    }

}
