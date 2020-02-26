package com.phlink.core.common.annotation;

import com.phlink.core.common.enums.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLogTrace {
    String description() default "";
    LogType type() default LogType.OPERATION;
}
