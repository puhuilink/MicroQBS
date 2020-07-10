/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:46
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:32
 */
package com.phlink.qbs.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.qbs.core.web.entity.RoleDepartment;
import com.phlink.qbs.core.web.mapper.RoleDepartmentMapper;
import com.phlink.qbs.core.web.service.RoleDepartmentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleDepartmentServiceImpl extends ServiceImpl<RoleDepartmentMapper, RoleDepartment>
        implements RoleDepartmentService {

    @Override
    public List<RoleDepartment> listByRoleId(String roleId) {
        QueryWrapper<RoleDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleDepartment::getRoleId, roleId);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deleteByRoleId(String roleId) {
        UpdateWrapper<RoleDepartment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(RoleDepartment::getRoleId, roleId);
        baseMapper.delete(updateWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deleteByDepartmentId(String departmentId) {
        UpdateWrapper<RoleDepartment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(RoleDepartment::getDepartmentId, departmentId);
        baseMapper.delete(updateWrapper);
    }
}
