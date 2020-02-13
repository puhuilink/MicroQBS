package com.phlink.bus.api.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DistributedLockParam {

    /**
     * 字段名称
     *
     * @return String
     */
    String name() default "";
}