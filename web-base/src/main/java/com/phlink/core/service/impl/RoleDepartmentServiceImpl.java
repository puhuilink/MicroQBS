package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.DepartmentMaster;
import com.phlink.core.entity.RoleDepartment;
import com.phlink.core.mapper.RoleDepartmentMapper;
import com.phlink.core.service.RoleDepartmentService;
import com.phlink.core.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleDepartmentServiceImpl extends ServiceImpl<RoleDepartmentMapper, RoleDepartment> implements RoleDepartmentService {

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