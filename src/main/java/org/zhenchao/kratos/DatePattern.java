package org.zhenchao.kratos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhenchao.wang 2016-09-06 09:18:08
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface DatePattern {

    String value();

}
