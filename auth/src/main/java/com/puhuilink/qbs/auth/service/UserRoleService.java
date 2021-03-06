package com.puhuilink.qbs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.auth.entity.Role;
import com.puhuilink.qbs.auth.entity.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId")
    List<Role> listByUserId(String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> listDepIdsByUserId(String userId);
}
