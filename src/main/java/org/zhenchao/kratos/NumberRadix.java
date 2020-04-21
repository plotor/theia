package org.zhenchao.kratos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhenchao.wang 2016-09-06 10:18:20
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface NumberRadix {

    enum NumberFormat {
        BINARY(2),
        OCTAL(8),
        DECIMAL(10),
        HEX(16);

        private final int radix;

        NumberFormat(int radix) {
            this.radix = radix;
        }

        public int radix() {
            return this.radix;
        }
    }

    NumberFormat value() default NumberFormat.DECIMAL;

}
