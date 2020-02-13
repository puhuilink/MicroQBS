package com.phlink.bus.api.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.domain.Tree;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.common.utils.TreeUtil;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.im.service.IImService;
import com.phlink.bus.api.system.dao.DeptMapper;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service("deptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Lazy
    @Autowired
    private IImService imService;
    @Autowired
    private BusApiProperties busApiProperties;
    @Lazy
    @Autowired
    private IImGroupsService imGroupsService;

    @Override
    public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Dept> depts = findDepts(dept, request);
            List<Tree<Dept>> trees = new ArrayList<>();
            buildTrees(trees, depts);
            Tree<Dept> deptTree = TreeUtil.build(trees);

            result.put("rows", deptTree);
            result.put("total", depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }

    @Override
    public List<Dept> findDepts(Dept dept, QueryRequest request) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName())) {
            queryWrapper.lambda().like(Dept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(Dept::getCreateTime, DateUtil.formatDateStr(dept.getCreateTimeFrom()))
                    .le(Dept::getCreateTime,  DateUtil.formatDateStr(dept.getCreateTimeTo()));
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", BusApiConstant.ORDER_ASC, true);
        return this.baseMapper.listDepts(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createDept(Dept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null) {
            dept.setParentId(0L);
        }
        dept.setCreateTime(new Date());
        this.save(dept);
        registerToImServer(dept, dept.getBuildGroup());
    }

    @Override
    public void registerToImServer(Dept dept, Boolean createGroup) {
        try {
            CommonResultEntity entity;
            if (createGroup != null && createGroup) {
                // 检查imGroup有没有建群
                createGroupForDept(dept);
                entity = imService.createDept(dept, true, busApiProperties.getIm().getBusOrgId());
            } else {
                // 检查imGroup有没有建群
                deleteGroupForDept(dept.getDeptId());
                entity = imService.createDept(dept, false, busApiProperties.getIm().getBusOrgId());
            }
            if ("10210".equals(entity.getCode()) || "10000".equals(entity.getCode())) {
                if (dept.getIm() == null || !dept.getIm()) {
                    dept.setIm(true);
                    this.baseMapper.updateById(dept);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createGroupForDept(Dept dept) {
        ImGroups groups = imGroupsService.getByDepartId(dept.getDeptId());
        if(groups == null) {
            // 创建
            groups = new ImGroups();
            groups.setDepartId(String.valueOf(dept.getDeptId()));
            groups.setType(GroupTypeEnum.DEPT);
            groups.setName(dept.getDeptName());
            groups.setCompanyId(busApiProperties.getIm().getBusOrgId());
            try {
                imGroupsService.createImGroups(groups);
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateToImServer(Dept dept, Boolean buildGroup) {
        try {
            CommonResultEntity entity;
            if (buildGroup != null && buildGroup) {
                createGroupForDept(dept);
                entity = imService.updateDept(dept, true);
            } else {
                deleteGroupForDept(dept.getDeptId());
                entity = imService.updateDept(dept, false);
            }
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 删除非自定义群
     * @Param: [deptId]
     * @Return: void
     * @Author wen
     * @Date 2020/1/3 18:10
     */
    public void deleteGroupForDept(Long deptId) {
        ImGroups groups = imGroupsService.getByDepartId(deptId);
        if (groups != null && !GroupTypeEnum.CUSTOMIZE.equals(groups.getType())) {
            // 删除
            log.info("删除部门非自定义群组");
            imGroupsService.removeById(groups.getId());
            try {
                imService.deleteGroup(groups.getGroupId());
            } catch (RedisConnectException e) {
                log.error("删除群组错误 {}", JSON.toJSONString(groups), e);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateDept(Dept dept) {
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
        updateToImServer(dept, dept.getBuildGroup());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteDepts(String[] deptIds) {
        Arrays.stream(deptIds).forEach(deptId -> {
            this.baseMapper.deleteDepts(deptId);
            try {
                //TODO 遍历旗下所有部门，并删除
                deleteGroupForDept(Long.valueOf(deptId));
                imService.deleteDept(deptId);
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<Dept> listChildrenDepts(Long deptId) {
        return this.baseMapper.listChildrenDepts(deptId);
    }

    @Override
    public Dept getByDeptName(String deptName) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dept::getDeptName, deptName);
        return getOne(queryWrapper);
    }

    private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {
        depts.forEach(dept -> {
            Tree<Dept> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getDeptName());
            tree.setCreateTime(dept.getCreateTime());
            tree.setModifyTime(dept.getModifyTime());
            tree.setOrder(dept.getOrderNum());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());
            trees.add(tree);
        });
    }
}
