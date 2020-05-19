/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:58
 * @FilePath: /phlink-common-framework/core-web/src/main/java/com/phlink/core/web/aop/LimitAspect.java
 */
package com.phlink.core.web.aop;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.phlink.core.base.annotation.Limit;
import com.phlink.core.base.entity.LimitType;
import com.phlink.core.base.exception.LimitAccessException;
import com.phlink.core.web.utils.IPUtil;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 接口限流
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.phlink.core.base.annotation.Limit)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        String ip = IPUtil.getIpAddr(request);
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        switch (limitType) {
            case IP:
                key = ip;
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        String keys = StringUtils.join(limitAnnotation.prefix() + "_", key, ip);
        String luaScript = buildLuaScript();
        RScript script = redissonClient.getScript(StringCodec.INSTANCE);
        Integer count = script.eval(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.VALUE,
                Collections.singletonList(keys), limitCount, limitPeriod);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, keys, name);
        if (count != null && count <= limitCount) {
            return point.proceed();
        } else {
            throw new LimitAccessException();
        }
    }

    /**
     * 限流脚本 调用的时候不超过阈值，则直接返回并执行计算器自加。
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        return "local c" + "\nc = redis.call('get',KEYS[1])" + "\nif c and tonumber(c) > tonumber(ARGV[1]) then"
                + "\nreturn c;" + "\nend" + "\nc = redis.call('incr',KEYS[1])" + "\nif tonumber(c) == 1 then"
                + "\nredis.call('expire',KEYS[1],ARGV[2])" + "\nend" + "\nreturn c;";
    }

}
