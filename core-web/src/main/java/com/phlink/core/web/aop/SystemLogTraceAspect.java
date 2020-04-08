package com.phlink.core.web.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.phlink.core.base.annotation.SystemLogTrace;
import com.phlink.core.base.utils.InheritableThreadLocalUtil;
import com.phlink.core.web.utils.IpInfoUtil;
import com.phlink.core.base.utils.ThreadPoolUtil;
import com.phlink.core.web.entity.LogTrace;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.service.LogTraceService;
import com.phlink.core.web.service.UserService;
import com.phlink.core.web.utils.SecurityUtil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

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
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtil securityUtil;
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
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";
        Integer type = null;

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            description = method.getAnnotation(SystemLogTrace.class).description();
            type = method.getAnnotation(SystemLogTrace.class).type().ordinal();
            map.put("description", description);
            map.put("type", type);
        }
        return map;
    }

    @Pointcut("@annotation(com.phlink.core.base.annotation.SystemLogTrace)")
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

        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        InheritableThreadLocalUtil.put(beginTime);
    }

    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            String username = "";
            String description = getControllerMethodInfo(joinPoint).get("description").toString();
            Map<String, String[]> logParams = request.getParameterMap();
            String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            // 判断允许不用登录的注解
            if ("anonymousUser".equals(principal) && !description.contains("短信登录")) {
                return;
            }
            if (!"anonymousUser".equals(principal)) {
                SecurityUser user = securityUtil.getSecurityUser();
                username = user.getUsername();
            }
            if (description.contains("短信登录")) {
                if (logParams.get("mobile") != null) {
                    String mobile = logParams.get("mobile")[0];
                    username = userService.getByMobile(mobile).getUsername() + "(" + mobile + ")";
                }
            }
            LogTrace logTrace = new LogTrace();

            //请求用户
            logTrace.setUsername(username);
            //日志标题
            logTrace.setName(description);
            //日志类型
            logTrace.setLogType((int) getControllerMethodInfo(joinPoint).get("type"));
            //日志请求url
            logTrace.setRequestUrl(request.getRequestURI());
            //请求方式
            logTrace.setRequestType(request.getMethod());
            //请求参数
            logTrace.setMapToParams(logParams);
            //请求IP
            String ip = IpInfoUtil.getIpAddr(request);
            logTrace.setIp(ip);
            //IP地址
            logTrace.setIpInfo(IpInfoUtil.getIpCity(ip));
            //请求开始时间
            long beginTime = InheritableThreadLocalUtil.get(Date.class).getTime();
            long endTime = System.currentTimeMillis();
            //请求耗时
            Long logElapsedTime = endTime - beginTime;
            logTrace.setCostTime(logElapsedTime.intValue());

            //调用线程保存至ES
            ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(logTrace, logTraceService));
        } catch (Exception e) {
            log.error("AOP后置通知异常", e);
        }
    }

    /**
     * 保存日志至数据库
     */
    private static class SaveSystemLogThread implements Runnable {

        private LogTrace logTrace;
        private LogTraceService logService;

        public SaveSystemLogThread(LogTrace logTrace, LogTraceService logService) {
            this.logTrace = logTrace;
            this.logService = logService;
        }

        @Override
        public void run() {
            logService.save(logTrace);
        }
    }
}
