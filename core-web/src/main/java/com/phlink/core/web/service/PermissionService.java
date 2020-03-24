package com.phlink.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.web.entity.Permission;
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

    /**
     * 通过层级查找
     * 默认升序
     * @param level
     * @return
     */
    List<Permission> listByLevelOrderBySortOrder(Integer level);

    /**
     * 通过parendId查找
     * @param parentId
     * @return
     */
    List<Permission> listByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    List<Permission> listByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    List<Permission> listByTitle(String title);

    /**
     * 模糊搜索
     * @param title
     * @return
     */
    List<Permission> listByTitleLikeOrderBySortOrder(String title);
}