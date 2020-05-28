/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:06
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/RedisLockAspect.java
 */
package com.phlink.core.web.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.phlink.core.base.annotation.DistributedLock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 分布式锁
 */
@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.phlink.core.base.annotation.DistributedLock)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        // 执行方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLockAnnotation = method.getAnnotation(DistributedLock.class);
        RLock lock = redissonClient.getLock(distributedLockAnnotation.key());
        try {
            boolean islock = lock.tryLock(distributedLockAnnotation.sleep(), distributedLockAnnotation.expire() * 1000,
                    TimeUnit.MILLISECONDS);
            if (islock) {
                result = point.proceed();
            } else {
                log.info("[RedisLock] 获取锁{}失败", distributedLockAnnotation.key());
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

}
