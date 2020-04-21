package org.zhenchao.kratos.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark that the visibility is only for test, can't be use in production.
 *
 * @author zhenchao.wang 2020-01-15 11:13
 * @version 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface TestingVisible {
}
