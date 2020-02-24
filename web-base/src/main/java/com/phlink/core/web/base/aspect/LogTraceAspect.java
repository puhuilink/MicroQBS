package com.phlink.core.web.base.aspect;

import com.phlink.core.web.base.entity.SysLogTrace;
import com.phlink.core.web.base.properties.PhlinkProperties;
import com.phlink.core.web.base.service.LogTraceService;
import com.phlink.core.web.common.utils.HttpContextUtil;
import com.phlink.core.web.common.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author Wen
 */
@Slf4j
@Aspect
@Component
public class LogTraceAspect {

    @Autowired
    private PhlinkProperties phlinkProperties;

    @Autowired
    private LogTraceService logTraceService;

    @Pointcut("@annotation(com.phlink.core.web.common.annotation.LogTrace)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (phlinkProperties.isOpenAopLog()) {
            // 保存日志
//            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
//            if (StringUtils.isNotBlank(token)) {
//                username = JWTUtil.getUsername(token);
//            }

            SysLogTrace log = new SysLogTrace();
            log.setUsername(username);
            log.setIp(ip);
            log.setTime(time);
            logTraceService.saveLog(point, log);
        }
        return result;
    }
}
