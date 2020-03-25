package com.phlink.core.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.entity.RoleDepartment;
import com.phlink.core.web.mapper.RoleDepartmentMapper;
import com.phlink.core.web.service.RoleDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wen
 */
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
