package com.phlink.bus.api.system.manager;

import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.phlink.bus.api.system.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 封装一些和 SystemConfig 相关的业务操作
 */
@Service
public class SystemConfigManager {

    @Autowired
    private CacheService cacheService;

    /**
     * 将系统相关信息添加到 Redis缓存中
     *
     * @param systemConfig
     */
    public void loadSystemRedisCache(SystemConfig systemConfig) throws Exception {
        // 缓存系统配置
        cacheService.saveSystemConfig(systemConfig);
    }

}
