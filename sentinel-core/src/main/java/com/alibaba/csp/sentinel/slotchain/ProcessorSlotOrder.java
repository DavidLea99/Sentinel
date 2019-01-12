package com.alibaba.csp.sentinel.slotchain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the sort order for a ProcessorSlot.
 *
 * Lower values have higher priority.
 * The default value is {@code ProcessorSlotOrder.LOWEST_PRECEDENCE}, indicating lowest priority
 *
 * @author cdfive
 * @date 2019-01-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ProcessorSlotOrder {

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * The order value. Lowest precedence by default.
     *
     * @return the order value
     */
    int value() default LOWEST_PRECEDENCE;
}
