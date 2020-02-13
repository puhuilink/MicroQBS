package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.Role;
import com.phlink.bus.api.system.domain.RoleMenu;
import com.phlink.bus.api.system.service.RoleMenuServie;
import com.phlink.bus.api.system.service.RoleService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuServie roleMenuServie;

    @ApiOperation(value = "分页列表", notes = "分页列表", tags = "角色管理", httpMethod = "GET")
    @GetMapping
    //@RequiresPermissions("role:view")
    public Map<String, Object> rolePage(QueryRequest queryRequest, Role role) {

        return getDataTable(roleService.findRoles(role, queryRequest));
    }

    @ApiOperation(value = "列表", notes = "列表", tags = "角色管理", httpMethod = "GET")
    @GetMapping("/all-list")
    //@RequiresPermissions("role:view")
    public Map<String, Object> roleList() {
        return getDataTable(roleService.list());
    }

    @ApiOperation(value = "检查角色名是否存在", notes = "检查角色名是否存在", tags = "角色管理", httpMethod = "GET")
    @GetMapping("check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @ApiOperation(value = "角色对应菜单列表", notes = "角色对应菜单列表", tags = "角色管理", httpMethod = "GET")
    @GetMapping("menu/{roleId}")
    public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(Long.valueOf(roleId));
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    @ApiOperation(value = "新增", notes = "新增", tags = "角色管理", httpMethod = "POST")
    @Log("新增角色")
    @PostMapping
    //@RequiresPermissions("role:add")
    public void addRole(@RequestBody @Valid Role role) throws BusApiException {
        try {
            // 页面添加的都是非系统角色，可删除
            role.setSysRole(false);
            this.roleService.createRole(role);
        } catch (Exception e) {
            String message = "新增角色失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "删除", notes = "删除", tags = "角色管理", httpMethod = "DELETE")
    @Log("删除角色")
    @DeleteMapping("/{roleIds}")
    //@RequiresPermissions("role:delete")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws BusApiException {
        try {
            String[] ids = roleIds.split(StringPool.COMMA);
            this.roleService.deleteRoles(ids);
        } catch (Exception e) {
            String message = "删除角色失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "修改", notes = "修改", tags = "角色管理", httpMethod = "PUT")
    @Log("修改角色")
    @PutMapping
    //@RequiresPermissions("role:update")
    public void updateRole(@RequestBody @Valid Role role) throws BusApiException {
        try {
            this.roleService.updateRole(role);
        } catch (Exception e) {
            String message = "修改角色失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "导出", notes = "导出", tags = "角色管理", httpMethod = "POST")
    @PostMapping("excel")
    //@RequiresPermissions("role:export")
    public void export(QueryRequest queryRequest, @RequestBody @Valid Role role, HttpServletResponse response) throws BusApiException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
