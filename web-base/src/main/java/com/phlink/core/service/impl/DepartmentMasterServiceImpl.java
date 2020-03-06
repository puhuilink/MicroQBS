package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.DepartmentMaster;
import com.phlink.core.mapper.DepartmentMapper;
import com.phlink.core.mapper.DepartmentMasterMapper;
import com.phlink.core.service.DepartmentMasterService;
import com.phlink.core.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DepartmentMasterServiceImpl extends ServiceImpl<DepartmentMasterMapper, DepartmentMaster> implements DepartmentMasterService {

    @Override
    public List<String> listMasterByDepartmentId(String departmentId, Integer type) {
        QueryWrapper<DepartmentMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DepartmentMaster::getDepartmentId, departmentId);
        queryWrapper.lambda().eq(DepartmentMaster::getType, type);
        List<DepartmentMaster> masters = baseMapper.selectList(queryWrapper);
        return masters.stream().map(DepartmentMaster::getUserId).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deleteByDepartmentId(String departmentId) {
        UpdateWrapper<DepartmentMaster> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(DepartmentMaster::getDepartmentId, departmentId);
        baseMapper.delete(updateWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deleteByUserId(String userId) {
        UpdateWrapper<DepartmentMaster> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(DepartmentMaster::getUserId, userId);
        baseMapper.delete(updateWrapper);
    }
}