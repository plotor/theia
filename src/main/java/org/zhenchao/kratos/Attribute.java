package org.zhenchao.kratos;

import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.inject.converter.Converter;
import org.zhenchao.kratos.inject.converter.VoidConverter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhenchao.wang 2016-09-06 09:10:54
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Attribute {

    /** Property name */
    String name() default "";

    /** Alias for {@link #name()} */
    String value() default "";

    /**
     * Configure required.
     *
     * {@code true} means this field must be configured, otherwise will throw {@link ConfigException}.
     */
    boolean required() default true;

    /** The default value when missing config. */
    String defaultValue() default "";

    /** Whether inject this field with {@link java.util.Properties} or {@link String} raw type. */
    boolean raw() default false;

    /** Convert the string to target field type. */
    Class<? extends Converter> converter() default VoidConverter.class;

}
