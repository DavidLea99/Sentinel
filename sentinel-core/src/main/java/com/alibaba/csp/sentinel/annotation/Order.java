package com.alibaba.csp.sentinel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the sort order for an annotated component.
 *
 * Lower values have higher priority.
 * The default value is {@code Order.LOWEST_PRECEDENCE}, indicating lowest priority.
 *
 * @author cdfive
 * @date 2019-01-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Order {

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * The order value. Lowest precedence by default.
     *
     * @return the order value
     */
    int value() default LOWEST_PRECEDENCE;
}
