package com.puhuilink.qbs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.auth.entity.RoleDepartment;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * 角色部门接口
 */
@CacheConfig(cacheNames = "roleDepartment")
public interface RoleDepartmentService extends IService<RoleDepartment> {
    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> listByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

}
