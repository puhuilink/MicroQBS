/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:14
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/SystemLogTraceAspect.java
 */
package com.puhuilink.qbs.core.logtrace.aop;

import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.utils.InheritableThreadLocalUtil;
import com.puhuilink.qbs.core.base.utils.IpInfoUtil;
import com.puhuilink.qbs.core.logtrace.entity.LogTrace;
import com.puhuilink.qbs.core.logtrace.service.LogTraceService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP 记录用户操作日志
 *
 * @author Wen
 */
@Slf4j
@Aspect
@Component
public class SystemLogTraceAspect {

    @Autowired
    private LogTraceService logTraceService;
    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(16);
        // 获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取相关参数
        Object[] arguments = joinPoint.getArgs();
        // 生成类对象
        Class targetClass = Class.forName(targetName);
        // 获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";
        Integer type = null;

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                // 比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            description = method.getAnnotation(SystemLogTrace.class).description();
            type = method.getAnnotation(SystemLogTrace.class).type().ordinal();
            map.put("description", description);
            map.put("type", type);
        }
        return map;
    }

    @Pointcut("@annotation(com.puhuilink.qbs.core.base.annotation.SystemLogTrace)")
    public void controllerAspect() {

    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        // 线程绑定变量（该数据只有当前请求的线程可见）
        Long start = System.currentTimeMillis();
        InheritableThreadLocalUtil.put(start);
    }

    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            String description = getControllerMethodInfo(joinPoint).get("description").toString();
            LogTrace logTrace = new LogTrace();
            Map<String, String[]> logParams;
            logParams = request.getParameterMap();
            // 日志请求url
            logTrace.setRequestUrl(request.getRequestURI());
            // 请求方式
            logTrace.setRequestType(request.getMethod());
            // 请求IP
            String ip = IpInfoUtil.getIpAddr(request);
            logTrace.setIp(ip);
            // 请求用户
            // TODO 获取请求登录用户
            String username = "";
            logTrace.setUsername(username);
            // 日志标题
            logTrace.setName(description);
            // 日志类型
            logTrace.setLogType((int) getControllerMethodInfo(joinPoint).get("type"));
            // 请求参数
            logTrace.setMapToParams(logParams);
            // 请求开始时间
            long beginTime = InheritableThreadLocalUtil.get(Long.class);
            long endTime = System.currentTimeMillis();
            // 请求耗时
            long logElapsedTime = endTime - beginTime;
            logTrace.setCostTime(logElapsedTime);
            logTrace.setCreateTime(LocalDateTime.now());
            logTraceService.save(logTrace);
        } catch (Exception e) {
            log.error("LogTrace 后置通知异常", e);
        }
    }
}
