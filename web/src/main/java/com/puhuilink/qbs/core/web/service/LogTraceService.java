package com.puhuilink.qbs.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import com.puhuilink.qbs.core.web.entity.LogTrace;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

@CacheConfig(cacheNames = "logTrace")
public interface LogTraceService extends IService<LogTrace> {

    List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo);

    void removeAll();
}
