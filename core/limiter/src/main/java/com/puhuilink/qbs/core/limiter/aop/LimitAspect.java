/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:58
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/LimitAspect.java
 */
package com.puhuilink.qbs.core.limiter.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.puhuilink.qbs.core.base.utils.IpInfoUtil;
import com.puhuilink.qbs.core.limiter.annotation.Limiter;
import com.puhuilink.qbs.core.limiter.config.properties.RateLimiterProperties;
import com.puhuilink.qbs.core.limiter.exception.LimitAccessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口限流
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {
    private static final Map<String, RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();
    @Autowired
    private RateLimiterProperties rateLimiterProperties;

    @Pointcut("@annotation(com.puhuilink.qbs.core.limiter.annotation.Limiter)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (!rateLimiterProperties.isEnable()) {
            return point.proceed();
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limiter limitAnnotation = method.getAnnotation(Limiter.class);
        String key = limitAnnotation.key();
        String ip = IpInfoUtil.getIpAddr(request);
        String keyType;
        double qbs;
        switch (limitAnnotation.type()) {
            case IP:
                keyType = ip;
                qbs = limitAnnotation.QPS();
                break;
            default:
                keyType = "";
                qbs = rateLimiterProperties.getQPS();
                break;
        }
        String limiterKey = StringUtils.join(key, keyType);
        RateLimiter rateLimiter;
        if (!rateLimiterCache.containsKey(limiterKey)) {
            // 以配置的为准
            rateLimiter = RateLimiter.create(qbs);
            rateLimiterCache.put(limiterKey, rateLimiter);
        } else {
            rateLimiter = rateLimiterCache.get(limiterKey);
        }
        boolean acquire = rateLimiter.tryAcquire(limitAnnotation.timeout(), limitAnnotation.timeunit());
        if (acquire) {
            return point.proceed();
        } else {
            log.warn("接口请求次数超过限制，限制QPS为:{}", qbs);
            throw new LimitAccessException("请求频繁，请稍后再试");
        }

    }

}
