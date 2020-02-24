package com.phlink.core.web.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.phlink.core.web.base.domain.SysLogTrace;
import com.phlink.core.web.base.mapper.LogTraceMapper;
import com.phlink.core.web.base.service.LogTraceService;
import com.phlink.core.web.base.utils.AddressUtil;
import com.phlink.core.web.common.annotation.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogTraceServiceImpl extends ServiceImpl<LogTraceMapper, SysLogTrace> implements LogTraceService {

    @Override
    @Transactional
    public void saveLog(ProceedingJoinPoint joinPoint, SysLogTrace sysLogTrace) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogTrace logAnnotation = method.getAnnotation(LogTrace.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLogTrace.setOperation(logAnnotation.value());
        }
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        sysLogTrace.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            sysLogTrace.setParams(params.toString());
        }
        sysLogTrace.setCreateTime(LocalDateTime.now());
        sysLogTrace.setLocation(AddressUtil.getCityInfo(sysLogTrace.getIp()));
        // 保存系统日志
        save(sysLogTrace);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames){
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        params.append(" ").append(paramNames.get(i)).append(": ").append(JSON.toJSONString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(JSON.toJSONString(args[i]));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
}


