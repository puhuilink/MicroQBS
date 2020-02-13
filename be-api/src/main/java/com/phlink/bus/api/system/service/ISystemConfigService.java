package com.phlink.bus.api.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.system.domain.Dict;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wen
 */
public interface ISystemConfigService extends IService<SystemConfig> {

    IPage<SystemConfig> findSystemConfigs(QueryRequest queryRequest, SystemConfig systemConfig);

    void createSystemConfig(SystemConfig systemConfig);

    void deleteSystemConfigs(String[] ids);

    void updateSystemConfig(SystemConfig systemConfig);
}
