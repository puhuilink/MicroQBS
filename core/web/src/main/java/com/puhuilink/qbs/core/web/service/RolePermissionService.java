package com.puhuilink.qbs.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.core.web.entity.RolePermission;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * 角色权限接口
 */
@CacheConfig(cacheNames = "rolePermission")
public interface RolePermissionService extends IService<RolePermission> {
    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> listByPermissionId(String permissionId);

    /**
     * 通过roleId获取
     * @param roleId
     */
    List<RolePermission> listByRoleId(String roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

}
