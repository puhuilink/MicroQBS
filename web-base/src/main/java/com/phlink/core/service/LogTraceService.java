package com.phlink.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.entity.LogTrace;
import com.phlink.core.entity.Permission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = "logTrace")
public interface LogTraceService extends IService<LogTrace> {
}
