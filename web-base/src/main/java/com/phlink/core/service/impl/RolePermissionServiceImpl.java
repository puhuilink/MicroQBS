package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.RoleDepartment;
import com.phlink.core.entity.RolePermission;
import com.phlink.core.mapper.RoleDepartmentMapper;
import com.phlink.core.mapper.RolePermissionMapper;
import com.phlink.core.service.RoleDepartmentService;
import com.phlink.core.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Override
    public List<RolePermission> listByPermissionId(String permissionId) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RolePermission::getPermissionId, permissionId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<RolePermission> listByRoleId(String roleId) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RolePermission::getRoleId, roleId);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deleteByRoleId(String roleId) {
        UpdateWrapper<RolePermission> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        baseMapper.delete(wrapper);
    }
}