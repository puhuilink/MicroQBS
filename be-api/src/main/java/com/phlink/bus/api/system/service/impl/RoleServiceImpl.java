package com.phlink.bus.api.system.service.impl;

import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.system.dao.RoleMapper;
import com.phlink.bus.api.system.dao.RoleMenuMapper;
import com.phlink.bus.api.system.domain.Role;
import com.phlink.bus.api.system.domain.RoleMenu;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.RoleMenuServie;
import com.phlink.bus.api.system.service.RoleService;
import com.phlink.bus.api.system.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuServie roleMenuService;
    @Lazy
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<Role> findRoles(Role role, QueryRequest request) {
        try {
            LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(role.getRoleName())) {
                queryWrapper.like(Role::getRoleName, role.getRoleName());
            }
            if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
                LocalDate endDay = DateUtil.formatDateStr(role.getCreateTimeTo());
                LocalDate startDay = DateUtil.formatDateStr(role.getCreateTimeFrom());
                if(startDay != null) {
                    queryWrapper
                            .ge(Role::getCreateTime, startDay);
                }
                if(endDay != null) {
                    queryWrapper
                            .le(Role::getCreateTime, endDay.plusDays(1));
                }
            }
            Page<Role> page = new Page<>();
            SortUtil.handlePageSort(request, page, "roleId", BusApiConstant.ORDER_ASC, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return null;
        }
    }

    @Override
    public List<Role> findUserRole(String userName) {
        return baseMapper.findUserRole(userName);
    }

    @Override
    public Role findByName(String roleName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
    }

    @Override
    public void createRole(Role role) {
        role.setCreateTime(new Date());
        this.save(role);

        String[] menuIds = role.getMenuId().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);
    }

    @Override
    public void deleteRoles(String[] roleIds) throws Exception {

        List<Long> list = Stream.of(roleIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // 查找这些角色关联了那些用户
        List<Long> userIds = this.userRoleService.findUserIdsByRoleId(list.toArray(new Long[0]));

        baseMapper.deleteBatchIds(list);

        this.roleMenuService.deleteRoleMenusByRoleId(list.toArray(new Long[0]));
        this.userRoleService.deleteUserRolesByRoleId(list.toArray(new Long[0]));

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);

    }

    @Override
    public void updateRole(Role role) throws Exception {
        // 查找这些角色关联了那些用户
        Long[] roleId = {role.getRoleId()};
        List<Long> userIds = this.userRoleService.findUserIdsByRoleId(roleId);

        role.setModifyTime(new Date());
        baseMapper.updateById(role);

        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getRoleId()));

        String[] menuIds = role.getMenuId().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    private void setRoleMenus(Role role, String[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(Long.valueOf(menuId));
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }
}
