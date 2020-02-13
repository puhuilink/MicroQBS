package com.phlink.bus.api.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.system.domain.Dict;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.phlink.bus.api.system.dao.SystemConfigMapper;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.ISystemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author wen
 */
@Slf4j
@Service("systemConfigService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {

    @Override
    public IPage<SystemConfig> findSystemConfigs(QueryRequest queryRequest, SystemConfig systemConfig) {

        try {
            LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(systemConfig.getKey())) {
                queryWrapper.eq(SystemConfig::getKey, systemConfig.getKey());
            }
            if (StringUtils.isNotBlank(systemConfig.getValue())) {
                queryWrapper.eq(SystemConfig::getValue, systemConfig.getValue());
            }
            if (StringUtils.isNotBlank(systemConfig.getDescription())) {
                queryWrapper.like(SystemConfig::getDescription, systemConfig.getDescription());
            }

            Page<SystemConfig> page = new Page<>();
            SortUtil.handlePageSort(queryRequest, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取系统配置信息失败", e);
            return null;
        }
    }

    @Override
    public void createSystemConfig(SystemConfig systemConfig) {
        User user = BusApiUtil.getCurrentUser();
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfig.setCreateBy(user.getUserId());
        this.save(systemConfig);
    }

    @Override
    @Transactional
    public void deleteSystemConfigs(String[] systemConfigIds) {
        List<String> list = Arrays.asList(systemConfigIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public void updateSystemConfig(SystemConfig systemConfig) {
        this.baseMapper.updateById(systemConfig);
    }
}
