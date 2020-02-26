package com.phlink.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.entity.Permission;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "userPermission")
public interface PermissionService extends IService<Permission> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId")
    List<Permission> listByUserId(String userId);
}