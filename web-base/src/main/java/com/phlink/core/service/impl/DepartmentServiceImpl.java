package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.Department;
import com.phlink.core.mapper.DepartmentMapper;
import com.phlink.core.service.DepartmentService;
import com.phlink.core.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public List<Department> listByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {
        // 数据权限
        List<String> depIds = new ArrayList<>();
        if(openDataFilter) {
            depIds = securityUtil.getDeparmentIds();
        }
        return listByParentIdAndIdInOrderBySortOrder(parentId, depIds);
    }

    @Override
    public List<Department> listByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {
        return listByParentIdAndStatusOrderBySortOrder(parentId, status);
    }

    @Override
    public List<Department> listByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {
        // 数据权限
        List<String> depIds = new ArrayList<>();
        if(openDataFilter) {
            depIds = securityUtil.getDeparmentIds();
        }
        return listByTitleLikeAndIdInOrderBySortOrder(title, depIds);

    }

    private List<Department> listByParentIdAndIdInOrderBySortOrder(String parentId, List<String> depIds) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Department::getParentId, parentId);
        if (depIds != null && !depIds.isEmpty()) {
            queryWrapper.lambda().in(Department::getId, depIds);
        }
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
    }

    private List<Department> listByTitleLikeAndIdInOrderBySortOrder(String title, List<String> depIds) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Department::getTitle, title);
        if (depIds != null && !depIds.isEmpty()) {
            queryWrapper.lambda().in(Department::getId, depIds);
        }
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
    }
}