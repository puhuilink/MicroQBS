package com.phlink.core.controller.manage;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.common.exception.BizException;
import com.phlink.core.common.vo.PageVO;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.RoleDepartment;
import com.phlink.core.entity.RolePermission;
import com.phlink.core.entity.UserRole;
import com.phlink.core.service.RoleDepartmentService;
import com.phlink.core.service.RolePermissionService;
import com.phlink.core.service.RoleService;
import com.phlink.core.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/all")
    @ApiOperation(value = "获取全部角色")
    public List<Role> roleGetAll() {
        return roleService.list();
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页获取角色")
    public PageInfo<Role> allPage(PageVO page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<Role> roleList = roleService.list();
        PageInfo<Role> pageInfo = new PageInfo(roleList);
        for (Role role : roleList) {
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.listByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.listByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return pageInfo;
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

    @PutMapping(value = "/update/rolePermission")
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

    @PutMapping(value = "/update/roleDepartment")
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

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存数据")
    public Role save(Role role) {
        roleService.save(role);
        return role;
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "更新数据")
    public Role update(Role entity) {

        roleService.updateById(entity);
        //手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("userRole:" + "*");

        return entity;
    }

    @DeleteMapping(value = "/delete/{ids}")
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