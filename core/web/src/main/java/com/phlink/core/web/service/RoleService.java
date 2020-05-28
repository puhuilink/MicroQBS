package com.phlink.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.web.entity.Role;
import org.springframework.cache.annotation.CacheConfig;

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