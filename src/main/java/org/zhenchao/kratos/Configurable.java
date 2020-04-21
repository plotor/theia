package org.zhenchao.kratos;

import org.zhenchao.kratos.constant.Constants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhenchao.wang 2016-09-06 09:07
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Configurable {

    /** The configuration resource, eg. GConf:PASSPORT.TEST:example */
    String resource() default "";

    /** Alias for {@link #resource()} */
    String value() default "";

    /**
     * Auto configure setting.
     *
     * {@code true} means the options will be detected and auto injected,
     * otherwise you should instantiate and configure the options by manual.
     */
    boolean autoConfigure() default true;

    /**
     * Autoload configuration when found source update.
     * {@code true} means ignore the {@link Constants#COMMONS_CONFIG_AUTOLOAD} config, default is {@link false}.
     */
    boolean autoload() default false;

}
