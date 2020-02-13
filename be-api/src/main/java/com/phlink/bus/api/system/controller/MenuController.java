package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.router.VueRouter;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.Menu;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.MenuService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
public class MenuController extends BaseController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MenuService menuService;

    @GetMapping("/{username}")
    public ArrayList<VueRouter<Menu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }

    @ApiOperation(value = "列表", notes = "列表", tags = "菜单管理", httpMethod = "GET")
    @GetMapping
    //@RequiresPermissions("menu:view")
    public Map<String, Object> menuList(Menu menu) {
        return this.menuService.findMenus(menu);
    }

    @ApiOperation(value = "新增", notes = "新增", tags = "菜单管理", httpMethod = "POST")
    @Log("新增菜单/按钮")
    @PostMapping
    //@RequiresPermissions("menu:add")
    public void addMenu(@RequestBody @Valid Menu menu) throws BusApiException {
        try {
            this.menuService.createMenu(menu);
        } catch (Exception e) {
            String message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "删除", notes = "删除", tags = "菜单管理", httpMethod = "DELETE")
    @Log("删除菜单/按钮")
    @DeleteMapping("/{menuIds}")
    //@RequiresPermissions("menu:delete")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws BusApiException {
        try {
            String[] ids = menuIds.split(StringPool.COMMA);
            this.menuService.deleteMeuns(ids);
        } catch (Exception e) {
            String message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "修改", notes = "修改", tags = "菜单管理", httpMethod = "PUT")
    @Log("修改菜单/按钮")
    @PutMapping
    //@RequiresPermissions("menu:update")
    public void updateMenu(@RequestBody @Valid Menu menu) throws BusApiException {
        try {
            this.menuService.updateMenu(menu);
        } catch (Exception e) {
            String message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "导出", notes = "导出", tags = "菜单管理", httpMethod = "POST")
    @PostMapping("excel")
    //@RequiresPermissions("menu:export")
    public void export(@RequestBody @Valid Menu menu, HttpServletResponse response) throws BusApiException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
            ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
