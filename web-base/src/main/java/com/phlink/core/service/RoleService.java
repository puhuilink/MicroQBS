package com.phlink.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "role")
public interface RoleService extends IService<Role> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> listByDefaultRole(Boolean defaultRole);
}