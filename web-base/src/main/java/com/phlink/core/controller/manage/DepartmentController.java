package com.phlink.core.controller.manage;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phlink.core.common.constant.CommonConstant;
import com.phlink.core.common.exception.BizException;
import com.phlink.core.common.utils.CommonUtil;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.vo.Result;
import com.phlink.core.entity.Department;
import com.phlink.core.entity.DepartmentMaster;
import com.phlink.core.entity.User;
import com.phlink.core.service.DepartmentMasterService;
import com.phlink.core.service.DepartmentService;
import com.phlink.core.service.RoleDepartmentService;
import com.phlink.core.service.UserService;
import com.phlink.core.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "部门管理接口")
@RequestMapping("/manage/department")
@CacheConfig(cacheNames = "department")
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
    private RedissonClient redissonClient;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/list/parentId/{parentId}")
    @ApiOperation(value = "通过parentId获取")
    public List<Department> listByParentId(@PathVariable String parentId,
                                          @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list = new ArrayList<>();
        User u = securityUtil.getCurrUser();
        String key = "department::" + parentId + ":" + u.getId() + "_" + openDataFilter;

        RBucket<String> rBucket = redissonClient.getBucket(key);
        String v = rBucket.get();
        if (StrUtil.isNotBlank(v)) {
            list = new Gson().fromJson(v, new TypeToken<List<Department>>() {
            }.getType());
            return list;
        }
        list = departmentService.listByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);
        redissonClient.getBucket(key).set(new Gson().toJson(list), 15L, TimeUnit.DAYS);
        return list;
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加")
    public Result<Object> add(Department department) {

        departmentService.save(department);
        // 同步该节点缓存
        User u = securityUtil.getCurrUser();
        redissonClient.getKeys().deleteByPattern("department::" + department.getParentId() + ":*");

        // 如果不是添加的一级 判断设置上级为父节点标识
        if (!CommonConstant.PARENT_ID.equals(department.getParentId())) {
            Department parent = departmentService.getById(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.updateById(parent);
                // 更新上级节点的缓存
                redissonClient.getKeys().deleteByPattern("department::" + parent.getParentId() + ":*");
            }
        }
        return ResultUtil.success("添加成功");
    }

    @PutMapping(value = "/edit")
    @ApiOperation(value = "编辑")
    public String edit(Department department,
                               @RequestParam(required = false) String[] mainHeader,
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
        // 手动删除所有部门缓存
        redissonClient.getKeys().deleteByPattern("department:" + "*");
        // 删除所有用户缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        return "编辑成功";
    }

    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public String delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            deleteRecursion(id, ids);
        }
        // 手动删除所有部门缓存
        redissonClient.getKeys().deleteByPattern("department:" + "*");
        // 删除数据权限缓存
        redissonClient.getKeys().deleteByPattern("userRole::depIds:" + "*");

        return "批量通过id删除数据成功";
    }

    public void deleteRecursion(String id, String[] ids) {

        List<User> list = userService.listByDepartmentId(id);
        if (list != null && list.size() > 0) {
            throw new BizException("删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.getById(id);
        Department parent = null;
        if (dep != null && StrUtil.isNotBlank(dep.getParentId())) {
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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "部门名模糊搜索")
    public List<Department> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list = departmentService.listByTitleLikeOrderBySortOrder("%" + title + "%", openDataFilter);
        list = setInfo(list);
        return list;
    }

    public List<Department> setInfo(List<Department> list) {

        // lambda表达式
        list.forEach(item -> {
            if (!CommonConstant.PARENT_ID.equals(item.getParentId())) {
                Department parent = departmentService.getById(item.getParentId());
                item.setParentTitle(parent.getTitle());
            } else {
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            item.setMainMaster(departmentMasterService.listMasterByDepartmentId(item.getId(), CommonConstant.MASTER_TYPE_MAIN));
            item.setViceMaster(departmentMasterService.listMasterByDepartmentId(item.getId(), CommonConstant.MASTER_TYPE_VICE));
        });
        return list;
    }
}