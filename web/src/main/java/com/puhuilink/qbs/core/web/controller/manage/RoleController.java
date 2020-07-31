/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:09
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:57
 */
package com.puhuilink.qbs.core.web.controller.manage;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.PageVO;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.entity.RoleDepartment;
import com.puhuilink.qbs.core.web.entity.RolePermission;
import com.puhuilink.qbs.core.web.entity.UserRole;
import com.puhuilink.qbs.core.web.service.RoleDepartmentService;
import com.puhuilink.qbs.core.web.service.RolePermissionService;
import com.puhuilink.qbs.core.web.service.RoleService;
import com.puhuilink.qbs.core.web.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "角色管理接口")
@RequestMapping("/api/manage/role")
@Transactional
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "")
    @ApiOperation(value = "获取全部角色")
    public Result listAll() {
        List<Role> roles = roleService.list();
        return Result.ok().data(roles);
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页获取角色")
    public Result allPage(PageVO pageVo) {

        PageInfo<Role> page = PageHelper
            .startPage(pageVo.getPageNumber(), pageVo.getPageSize(), pageVo.getSort() + " " + pageVo.getOrder())
            .doSelectPageInfo(() -> roleService.list());
        for (Role role : page.getList()) {
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.listByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.listByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return Result.ok().data(page);
    }

    @PostMapping(value = "/default")
    @ApiOperation(value = "设置或取消默认角色")
    @SystemLogTrace(description = "设置或取消默认角色", type = LogType.OPERATION)
    public Result setDefault(@RequestParam String id, @RequestParam Boolean isDefault) {

        Role role = roleService.getById(id);
        if (role == null) {
            return Result.error("角色不存在");
        }
        role.setDefaultRole(isDefault);
        roleService.updateById(role);
        return Result.ok("设置成功");
    }

    @PutMapping(value = "/role-permission")
    @ApiOperation(value = "编辑角色分配菜单权限")
    @SystemLogTrace(description = "编辑角色分配菜单权限", type = LogType.OPERATION)
    public Result updateRolePermission(@RequestParam String roleId, @RequestParam(required = false) String[] permIds) {

        // 删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        // 分配新权限
        for (String permId : permIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            rolePermissionService.save(rolePermission);
        }
        // 手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");
        redissonClient.getKeys().deleteByPattern("userPermission:" + "*");
        redissonClient.getKeys().deleteByPattern("permission::userMenuList:*");

        return Result.ok("编辑成功");
    }

    @PutMapping(value = "/role-department")
    @ApiOperation(value = "编辑角色分配数据权限")
    @SystemLogTrace(description = "编辑角色分配数据权限", type = LogType.OPERATION)
    public String updateRoleDep(@RequestParam String roleId, @RequestParam Integer dataType,
                                @RequestParam(required = false) String[] depIds) {

        Role r = roleService.getById(roleId);
        r.setDataType(dataType);
        roleService.updateById(r);
        // 删除其关联数据权限
        roleDepartmentService.deleteByRoleId(roleId);
        // 分配新数据权限
        for (String depId : depIds) {
            RoleDepartment roleDepartment = new RoleDepartment();
            roleDepartment.setRoleId(roleId);
            roleDepartment.setDepartmentId(depId);
            roleDepartmentService.save(roleDepartment);
        }
        // 手动删除相关缓存
        redissonClient.getKeys().deleteByPattern("department:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");

        return "编辑成功";
    }

    @PostMapping(value = "")
    @ApiOperation(value = "保存数据")
    @SystemLogTrace(description = "保存角色数据", type = LogType.OPERATION)
    public Role save(Role role) {
        roleService.save(role);
        return role;
    }

    @PutMapping(value = "")
    @ApiOperation(value = "更新数据")
    @SystemLogTrace(description = "更新角色数据", type = LogType.OPERATION)
    public Role update(Role entity) {

        roleService.updateById(entity);
        // 手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");

        return entity;
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "批量通过ids删除")
    @SystemLogTrace(description = "批量删除角色数据", type = LogType.OPERATION)
    public String delByIds(@PathVariable String[] ids) {

        List<UserRole> list = userRoleService.listByIds(Convert.toList(String.class, ids));
        if (list != null && list.size() > 0) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "删除失败，包含正被用户使用关联的角色");
        }
        roleService.removeByIds(Convert.toList(String.class, ids));
        for (String id : ids) {
            // 删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            // 删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
        }
        return "批量通过id删除数据成功";
    }

}
