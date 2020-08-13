/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:36
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:25
 */
package com.puhuilink.qbs.auth.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.auth.entity.Permission;
import com.puhuilink.qbs.auth.mapper.PermissionMapper;
import com.puhuilink.qbs.auth.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("permissionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> listByUserId(String userId) {

        return permissionMapper.listByUserId(userId);
    }

    @Override
    public List<Permission> listByLevelOrderBySortOrder(Integer level) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getLevel, level);
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }

    @Override
    public List<Permission> listByParentIdOrderBySortOrder(String parentId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getParentId, parentId);
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }

    @Override
    public List<Permission> listByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getType, type);
        queryWrapper.lambda().eq(Permission::getStatus, status);
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }

    @Override
    public List<Permission> listByTitle(String title) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getTitle, title);
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }

    @Override
    public List<Permission> listByTitleLikeOrderBySortOrder(String title) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(Permission::getTitle, title);
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }
}
