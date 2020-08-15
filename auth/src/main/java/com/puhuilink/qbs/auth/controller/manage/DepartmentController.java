/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:47
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:41
 */
package com.puhuilink.qbs.auth.controller.manage;


import com.puhuilink.qbs.auth.entity.Department;
import com.puhuilink.qbs.auth.entity.DepartmentMaster;
import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.service.DepartmentMasterService;
import com.puhuilink.qbs.auth.service.DepartmentService;
import com.puhuilink.qbs.auth.service.RoleDepartmentService;
import com.puhuilink.qbs.auth.service.UserService;
import com.puhuilink.qbs.auth.utils.SecurityUtil;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.common.utils.CommonConstant;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.common.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "部门管理接口")
@RequestMapping("/api/manage/department")
@Transactional
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleDepartmentService roleDepartmentService;
    @Autowired
    private DepartmentMasterService departmentMasterService;
    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/parent/{parentId}")
    @ApiOperation(value = "通过parentId获取")
    public Result listByParentId(@PathVariable String parentId,
                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {
        User u = securityUtil.getCurrUser();
        List<Department> list = departmentService.listByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);
        return Result.ok().data(list);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "添加")
    @SystemLogTrace(description = "添加部门", type = LogType.OPERATION)
    public Result add(Department department) {
        // 同步该节点缓存
        departmentService.save(department);

        // 如果不是添加的一级 判断设置上级为父节点标识
        if (!CommonConstant.PARENT_ID.equals(department.getParentId())) {
            Department parent = departmentService.getById(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.updateById(parent);
            }
        }
        return Result.ok("添加成功");
    }

    @PutMapping(value = "")
    @ApiOperation(value = "编辑")
    @SystemLogTrace(description = "编辑部门", type = LogType.OPERATION)
    public Result edit(Department department, @RequestParam(required = false) String[] mainHeader,
                       @RequestParam(required = false) String[] viceHeader) {

        departmentService.updateById(department);
        // 先删除原数据
        departmentMasterService.deleteByDepartmentId(department.getId());
        for (String id : mainHeader) {
            DepartmentMaster dh = new DepartmentMaster();
            dh.setUserId(id);
            dh.setDepartmentId(department.getId());
            dh.setType(CommonConstant.MASTER_TYPE_MAIN);
            departmentMasterService.save(dh);
        }
        for (String id : viceHeader) {
            DepartmentMaster dh = new DepartmentMaster();
            dh.setUserId(id);
            dh.setDepartmentId(department.getId());
            dh.setType(CommonConstant.MASTER_TYPE_VICE);
            departmentMasterService.save(dh);
        }
        return Result.ok("编辑成功");
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "批量通过id删除")
    @SystemLogTrace(description = "批量删除部门", type = LogType.OPERATION)
    public Result delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            deleteRecursion(id, ids);
        }
        return Result.ok("批量通过id删除数据成功");
    }

    public void deleteRecursion(String id, String[] ids) {
        List<User> list = userService.listByDepartmentId(id);
        if (list == null || list.isEmpty()) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.getById(id);
        Department parent = null;
        if (dep != null && StringUtils.isNotBlank(dep.getParentId())) {
            parent = departmentService.getById(dep.getParentId());
        }
        departmentService.removeById(id);
        // 删除关联数据权限
        roleDepartmentService.deleteByDepartmentId(id);
        // 删除关联部门负责人
        departmentMasterService.deleteByDepartmentId(id);
        // 判断父节点是否还有子节点
        if (parent != null) {
            List<Department> childrenDeps = departmentService.listByParentIdOrderBySortOrder(parent.getId(), false);
            if (childrenDeps == null || childrenDeps.size() == 0) {
                parent.setIsParent(false);
                departmentService.updateById(parent);
            }
        }
        // 递归删除
        List<Department> departments = departmentService.listByParentIdOrderBySortOrder(id, false);
        for (Department d : departments) {
            if (!CommonUtil.judgeIds(d.getId(), ids)) {
                deleteRecursion(d.getId(), ids);
            }
        }
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "部门名模糊搜索")
    public Result searchByTitle(@RequestParam String title,
                                @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {
        List<Department> list = departmentService.listByTitleLikeOrderBySortOrder(title, openDataFilter);
        return Result.ok().data(setInfo(list));
    }

    public List<Department> setInfo(List<Department> list) {
        list.forEach(item -> {
            if (!CommonConstant.PARENT_ID.equals(item.getParentId())) {
                Department parent = departmentService.getById(item.getParentId());
                item.setParentTitle(parent.getTitle());
            } else {
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            item.setMainMaster(
                    departmentMasterService.listMasterByDepartmentId(item.getId(), CommonConstant.MASTER_TYPE_MAIN));
            item.setViceMaster(
                    departmentMasterService.listMasterByDepartmentId(item.getId(), CommonConstant.MASTER_TYPE_VICE));
        });
        return list;
    }
}
