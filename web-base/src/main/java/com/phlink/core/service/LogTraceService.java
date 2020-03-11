package com.phlink.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.common.vo.SearchVO;
import com.phlink.core.entity.LogTrace;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

@CacheConfig(cacheNames = "logTrace")
public interface LogTraceService extends IService<LogTrace> {

    List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo);

    void removeAll();
}
