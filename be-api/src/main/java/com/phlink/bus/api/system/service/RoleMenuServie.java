package com.phlink.bus.api.system.service;

import com.phlink.bus.api.system.domain.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleMenuServie extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(Long[] roleIds);

    void deleteRoleMenusByMenuId(Long[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(Long roleId);
}
