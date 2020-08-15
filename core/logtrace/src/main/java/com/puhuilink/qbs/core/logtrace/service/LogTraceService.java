package com.puhuilink.qbs.core.logtrace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import com.puhuilink.qbs.core.logtrace.entity.LogTrace;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

public interface LogTraceService extends IService<LogTrace> {

    List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo);

    void removeAll();
}
