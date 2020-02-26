package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.LogTrace;
import com.phlink.core.mapper.LogTraceMapper;
import com.phlink.core.service.LogTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogTraceServiceImpl extends ServiceImpl<LogTraceMapper, LogTrace> implements LogTraceService {


}


