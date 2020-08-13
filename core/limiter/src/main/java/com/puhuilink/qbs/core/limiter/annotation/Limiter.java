package com.puhuilink.qbs.core.limiter.annotation;



import com.puhuilink.qbs.core.limiter.enums.LimiterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    // 限流标志
    String key() default "";

    // 限制每秒访问次数
    int QPS() default 10;

    // 获取令牌超时时间 默认：1
    long timeout() default 1;

    // 获取令牌超时时间单位，默认：秒
    TimeUnit timeunit() default TimeUnit.SECONDS;

    // 限流类型，默认全局限流
    LimiterType type() default LimiterType.ALL;
}
