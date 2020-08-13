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

    // 获取令牌超时时间 默认：100
    long timeout() default 100;

    // 获取令牌超时时间单位，默认：毫秒
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 限流类型，默认全局限流
    LimiterType type() default LimiterType.ALL;
}
