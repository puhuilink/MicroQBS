package com.phlink.bus.api.system.manager;

import com.phlink.bus.api.common.domain.router.RouterMeta;
import com.phlink.bus.api.common.domain.router.VueRouter;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.utils.TreeUtil;
import com.phlink.bus.api.system.domain.Menu;
import com.phlink.bus.api.system.domain.Role;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.domain.UserConfig;
import com.phlink.bus.api.system.service.MenuService;
import com.phlink.bus.api.system.service.RoleService;
import com.phlink.bus.api.system.service.UserConfigService;
import com.phlink.bus.api.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

    @Autowired
    private CacheService cacheService;
    @Lazy
    @Autowired
    private RoleService roleService;
    @Lazy
    @Autowired
    private MenuService menuService;
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;


    /**
     * 通过用户名获取用户基本信息
     *
     * @param username 用户名
     * @return 用户基本信息
     */
    public User getUser(String username) {
        return this.userService.findByName(username);
    }

    public User getUserByMobile(String mobile) {
        return this.userService.findByMobile(mobile);

    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    public Set<String> getUserRoles(String username) {
        List<Role> roleList = this.roleService.findUserRole(username);
        return roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    public Set<String> getUserPermissions(String username) {
        List<Menu> permissionList = this.menuService.findUserPermissions(username);
        Set<String> result = new HashSet<>();
        for(Menu menu : permissionList) {
            String perms = menu.getPerms();
            if(StringUtils.isNotBlank(perms)) {
                if(perms.contains(";")) {
                    String[] permsArray = perms.split(";");
                    result.addAll(Arrays.asList(permsArray));
                }else{
                    result.add(perms);
                }
            }
        }
        return result;
    }

    /**
     * 通过用户名构建 Vue路由
     *
     * @param username 用户名
     * @return 路由集合
     */
    public ArrayList<VueRouter<Menu>> getUserRouters(String username) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        List<Menu> menus = this.menuService.findUserMenus(username);
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId(menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(true, true));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }

    /**
     * 通过用户 ID获取前端系统个性化配置
     *
     * @param userId 用户 ID
     * @return 前端系统个性化配置
     */
    public UserConfig getUserConfig(String userId) {
        return this.userConfigService.findByUserId(userId);
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param user user
     */
    @Async
    public void  loadUserRedisCache(User user) throws Exception {
        // 缓存用户
        cacheService.saveUser(user.getUsername());
        // 缓存用户
        if(StringUtils.isNotBlank(user.getMobile())) {
            cacheService.saveUser(user.getMobile());
        }
        // 缓存用户角色
        cacheService.saveRoles(user.getUsername());
        // 缓存用户权限
        cacheService.savePermissions(user.getUsername());
        // 缓存用户个性化配置
        cacheService.saveUserConfigs(String.valueOf(user.getUserId()));
    }

    /**
     * 将用户角色和权限添加到 Redis缓存中
     *
     * @param userIds userIds
     */
    public void loadUserPermissionRoleRedisCache(List<Long> userIds) throws Exception {
        for (Long userId : userIds) {
            User user = userService.getById(userId);
            if(user != null) {
                // 缓存用户角色
                cacheService.saveRoles(user.getUsername());
                // 缓存用户权限
                cacheService.savePermissions(user.getUsername());
            }
        }
    }

    /**
     * 通过用户 id集合批量删除用户 Redis缓存
     *
     * @param userIds userIds
     */
    public void deleteUserRedisCache(String... userIds) throws Exception {
        for (String userId : userIds) {
            User user = userService.getById(Long.parseLong(userId));
            if (user != null) {
                cacheService.deleteUser(user.getUsername());
                cacheService.deleteRoles(user.getUsername());
                cacheService.deletePermissions(user.getUsername());
            }
            cacheService.deleteUserConfigs(userId);
        }
    }
}
