package com.phlink.core.web.base.service;

import com.phlink.core.web.base.domain.SysLogTrace;
import org.aspectj.lang.ProceedingJoinPoint;

public interface LogTraceService {
    void saveLog(ProceedingJoinPoint point, SysLogTrace log);
}
