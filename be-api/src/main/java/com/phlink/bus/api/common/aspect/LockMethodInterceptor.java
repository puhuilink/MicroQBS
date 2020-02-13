package com.phlink.bus.api.common.aspect;

import com.phlink.bus.api.common.annotation.DistributedLock;
import com.phlink.bus.api.common.cache.CacheKeyGenerator;
import com.phlink.bus.api.common.exception.BusApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
public class LockMethodInterceptor {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CacheKeyGenerator cacheKeyGenerator;


    @Around("within(*) && @annotation(com.phlink.bus.api.common.annotation.DistributedLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        if (StringUtils.isEmpty(distributedLock.prefix())) {
            throw new RuntimeException("lock key don't null...");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        Object result = null;
        try {
            locked = lock.tryLock(distributedLock.waitTimeOut(), distributedLock.leaseTimeOut(), distributedLock.timeUnit());
            if (locked) {
                /** * 处理核心业务的逻辑。 */
                result = pjp.proceed();
            } else {
                log.info("DistributedLockTraitHandler: no distributed lock was obtained.");
                throw new BusApiException("重复提交，请稍后操作！");
            }
        } catch (InterruptedException ex) {
            log.info(ex.getMessage(), ex);
            throw ex;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw e;
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        return result;
    }
}
