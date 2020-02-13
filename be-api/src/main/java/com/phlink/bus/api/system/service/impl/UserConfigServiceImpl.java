package com.phlink.bus.api.system.service.impl;

import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.system.dao.UserConfigMapper;
import com.phlink.bus.api.system.domain.UserConfig;
import com.phlink.bus.api.system.service.UserConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("userConfigService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

    @Autowired
    private CacheService cacheService;

    @Override
    public UserConfig findByUserId(String userId) {
        return baseMapper.selectById(Long.parseLong(userId));
    }

    @Override
    @Transactional
    public void initDefaultUserConfig(String userId) {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserId(Long.valueOf(userId));
        userConfig.setColor(UserConfig.DEFAULT_COLOR);
        userConfig.setFixHeader(UserConfig.DEFAULT_FIX_HEADER);
        userConfig.setFixSiderbar(UserConfig.DEFAULT_FIX_SIDERBAR);
        userConfig.setLayout(UserConfig.DEFAULT_LAYOUT);
        userConfig.setTheme(UserConfig.DEFAULT_THEME);
        userConfig.setMultiPage(UserConfig.DEFAULT_MULTIPAGE);
        baseMapper.insert(userConfig);
    }

    @Override
    @Transactional
    public void deleteByUserId(String... userIds) {
        List<Long> list = Stream.of(userIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void update(UserConfig userConfig) throws Exception {
        baseMapper.updateById(userConfig);
        cacheService.saveUserConfigs(String.valueOf(userConfig.getUserId()));
    }
}
