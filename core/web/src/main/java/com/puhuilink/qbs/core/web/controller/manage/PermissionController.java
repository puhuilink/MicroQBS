/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:05
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:54
 */
package com.puhuilink.qbs.core.web.controller.manage;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.constant.CommonConstant;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.web.controller.vo.MenuVO;
import com.puhuilink.qbs.core.web.entity.Permission;
import com.puhuilink.qbs.core.web.entity.RolePermission;
import com.puhuilink.qbs.core.web.entity.User;
import com.puhuilink.qbs.core.web.security.permission.MySecurityMetadataSource;
import com.puhuilink.qbs.core.web.service.PermissionService;
import com.puhuilink.qbs.core.web.service.RolePermissionService;
import com.puhuilink.qbs.core.web.utils.SecurityUtil;
import com.puhuilink.qbs.core.web.utils.VoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "菜单/权限管理接口")
@RequestMapping("/api/manage/permission")
@CacheConfig(cacheNames = "permission")
@Transactional
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/user")
    @ApiOperation(value = "获取用户页面菜单数据")
    public Result getAllMenuList() {

        List<MenuVO> menuList = new ArrayList<>();
        // 读取缓存
        User u = securityUtil.getCurrUser();
        String key = "permission::userMenuList:" + u.getId();
        RBucket<String> rBucket = redissonClient.getBucket(key);
        String v = rBucket.get();
        if (StrUtil.isNotBlank(v)) {
            menuList = new Gson().fromJson(v, new TypeToken<List<MenuVO>>() {
            }.getType());
            return Result.ok().data(menuList);
        }

        // 用户所有权限 已排序去重
        List<Permission> list = permissionService.listByUserId(u.getId());

        // 筛选0级页面
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_NAV.equals(p.getType()) && CommonConstant.LEVEL_ZERO.equals(p.getLevel())) {
                menuList.add(VoUtil.permissionToMenuVO(p));
            }
        }
        // 筛选一级页面
        List<MenuVO> firstMenuList = new ArrayList<>();
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_PAGE.equals(p.getType()) && CommonConstant.LEVEL_ONE.equals(p.getLevel())) {
                firstMenuList.add(VoUtil.permissionToMenuVO(p));
            }
        }
        // 筛选二级页面
        List<MenuVO> secondMenuList = new ArrayList<>();
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_PAGE.equals(p.getType()) && CommonConstant.LEVEL_TWO.equals(p.getLevel())) {
                secondMenuList.add(VoUtil.permissionToMenuVO(p));
            }
        }
        // 筛选二级页面拥有的按钮权限
        List<MenuVO> buttonPermissions = new ArrayList<>();
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_OPERATION.equals(p.getType())
                && CommonConstant.LEVEL_THREE.equals(p.getLevel())) {
                buttonPermissions.add(VoUtil.permissionToMenuVO(p));
            }
        }

        // 匹配二级页面拥有权限
        for (MenuVO m : secondMenuList) {
            List<String> permTypes = new ArrayList<>();
            for (MenuVO me : buttonPermissions) {
                if (m.getId().equals(me.getParentId())) {
                    permTypes.add(me.getButtonType());
                }
            }
            m.setPermTypes(permTypes);
        }
        // 匹配一级页面拥有二级页面
        for (MenuVO m : firstMenuList) {
            List<MenuVO> secondMenu = new ArrayList<>();
            for (MenuVO me : secondMenuList) {
                if (m.getId().equals(me.getParentId())) {
                    secondMenu.add(me);
                }
            }
            m.setChildren(secondMenu);
        }
        // 匹配0级页面拥有一级页面
        for (MenuVO m : menuList) {
            List<MenuVO> firstMenu = new ArrayList<>();
            for (MenuVO me : firstMenuList) {
                if (m.getId().equals(me.getParentId())) {
                    firstMenu.add(me);
                }
            }
            m.setChildren(firstMenu);
        }

        // 缓存
        rBucket.set(new Gson().toJson(menuList), 15L, TimeUnit.DAYS);
        return Result.ok().data(menuList);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限菜单树")
    @Cacheable(key = "'allList'")
    public Result listAll() {

        // 0级
        List<Permission> list0 = permissionService.listByLevelOrderBySortOrder(CommonConstant.LEVEL_ZERO);
        for (Permission p0 : list0) {
            // 一级
            List<Permission> list1 = permissionService.listByParentIdOrderBySortOrder(p0.getId());
            p0.setChildren(list1);
            // 二级
            for (Permission p1 : list1) {
                List<Permission> children1 = permissionService.listByParentIdOrderBySortOrder(p1.getId());
                p1.setChildren(children1);
                // 三级
                for (Permission p2 : children1) {
                    List<Permission> children2 = permissionService.listByParentIdOrderBySortOrder(p2.getId());
                    p2.setChildren(children2);
                }
            }
        }
        return Result.ok().data(list0);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "添加")
    @CacheEvict(key = "'menuList'")
    @SystemLogTrace(description = "添加权限", type = LogType.OPERATION)
    public Result add(Permission permission) {

        // 判断拦截请求的操作权限按钮名是否已存在
        if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())) {
            List<Permission> list = permissionService.listByTitle(permission.getTitle());
            if (list != null && list.size() > 0) {
                throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "名称已存在");
            }
        }
        permissionService.save(permission);
        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        // 手动删除缓存
        redissonClient.getKeys().delete("permission::allList");
        return Result.ok().data(permission);
    }

    @PutMapping(value = "")
    @ApiOperation(value = "编辑")
    @SystemLogTrace(description = "编辑权限", type = LogType.OPERATION)
    public Permission update(Permission permission) {

        // 判断拦截请求的操作权限按钮名是否已存在
        if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())) {
            // 若名称修改
            Permission p = permissionService.getById(permission.getId());
            if (!p.getTitle().equals(permission.getTitle())) {
                List<Permission> list = permissionService.listByTitle(permission.getTitle());
                if (list != null && list.size() > 0) {
                    throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "名称已存在");
                }
            }
        }
        permissionService.updateById(permission);
        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        // 手动批量删除缓存
        redissonClient.getKeys().deleteByPattern("userPermission:" + "*");
        redissonClient.getKeys().deleteByPattern("user:" + "*");
        redissonClient.getKeys().deleteByPattern("permission::userMenuList:*");
        redissonClient.getKeys().delete("permission::allList");
        return permission;
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    @CacheEvict(key = "'menuList'")
    @SystemLogTrace(description = "批量删除权限", type = LogType.OPERATION)
    public Result deleteByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            List<RolePermission> list = rolePermissionService.listByPermissionId(id);
            if (list != null && list.size() > 0) {
                throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "删除失败，包含正被角色使用关联的菜单或权限");
            }
        }
        for (String id : ids) {
            permissionService.removeById(id);
        }
        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        // 手动删除缓存
        redissonClient.getKeys().delete("permission::allList");
        return Result.ok("批量通过id删除数据成功");
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "搜索菜单")
    public Result searchPermission(@RequestParam String title) {

        List<Permission> list = permissionService.listByTitleLikeOrderBySortOrder("%" + title + "%");
        return Result.ok().data(list);
    }
}
