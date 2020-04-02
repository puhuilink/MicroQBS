package com.phlink.core.web.controller.manage;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.web.base.exception.BizException;
import com.phlink.core.web.base.vo.PageVO;
import com.phlink.core.web.entity.Role;
import com.phlink.core.web.entity.RoleDepartment;
import com.phlink.core.web.entity.RolePermission;
import com.phlink.core.web.entity.UserRole;
import com.phlink.core.web.service.RoleDepartmentService;
import com.phlink.core.web.service.RolePermissionService;
import com.phlink.core.web.service.RoleService;
import com.phlink.core.web.service.UserRoleService;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
@RestController
@Api(tags = "角色管理接口")
@RequestMapping("/manage/role")
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
    public List<Role> listAll() {
        return roleService.list();
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页获取角色")
    public PageInfo<Role> allPage(PageVO pageVo) {

        PageInfo<Role> page = PageHelper.startPage(pageVo.getPageNumber(), pageVo.getPageSize(), pageVo.getSort() + " " + pageVo.getOrder())
                .doSelectPageInfo(() -> roleService.list());
        for (Role role : page.getList()) {
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.listByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.listByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return page;
    }

    @PostMapping(value = "/default")
    @ApiOperation(value = "设置或取消默认角色")
    public String setDefault(@RequestParam String id,
                             @RequestParam Boolean isDefault) {

        Role role = roleService.getById(id);
        if (role == null) {
            return "角色不存在";
        }
        role.setDefaultRole(isDefault);
        roleService.updateById(role);
        return "设置成功";
    }

    @PutMapping(value = "/role-permission")
    @ApiOperation(value = "编辑角色分配菜单权限")
    public String updateRolePermission(@RequestParam String roleId,
                                       @RequestParam(required = false) String[] permIds) {

        //删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        //分配新权限
        for (String permId : permIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            rolePermissionService.save(rolePermission);
        }
        //手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");
        redissonClient.getKeys().deleteByPattern("userPermission:" + "*");
        redissonClient.getKeys().deleteByPattern("permission::userMenuList:*");

        return "编辑成功";
    }

    @PutMapping(value = "/role-department")
    @ApiOperation(value = "编辑角色分配数据权限")
    public String updateRoleDep(@RequestParam String roleId,
                                @RequestParam Integer dataType,
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
    public Role save(Role role) {
        roleService.save(role);
        return role;
    }

    @PutMapping(value = "")
    @ApiOperation(value = "更新数据")
    public Role update(Role entity) {

        roleService.updateById(entity);
        //手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");

        return entity;
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "批量通过ids删除")
    public String delByIds(@PathVariable String[] ids) {

        List<UserRole> list = userRoleService.listByIds(Convert.toList(String.class, ids));
        if (list != null && list.size() > 0) {
            throw new BizException("删除失败，包含正被用户使用关联的角色");
        }
        roleService.removeByIds(Convert.toList(String.class, ids));
        for (String id : ids) {
            //删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            //删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
        }
        return "批量通过id删除数据成功";
    }

}
