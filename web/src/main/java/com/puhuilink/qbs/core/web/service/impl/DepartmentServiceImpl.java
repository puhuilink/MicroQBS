/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:20
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:07
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.web.entity.Department;
import com.puhuilink.qbs.core.web.mapper.DepartmentMapper;
import com.puhuilink.qbs.core.web.service.DepartmentService;
import com.puhuilink.qbs.core.web.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public List<Department> listByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {
        // 数据权限
        List<String> depIds = new ArrayList<>();
        if (openDataFilter) {
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
        if (openDataFilter) {
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
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }

    private List<Department> listByTitleLikeAndIdInOrderBySortOrder(String title, List<String> depIds) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(Department::getTitle, title);
        if (depIds != null && !depIds.isEmpty()) {
            queryWrapper.lambda().in(Department::getId, depIds);
        }
        queryWrapper.orderByDesc("sort_order");
        return this.list(queryWrapper);
    }
}
