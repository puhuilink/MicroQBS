package com.phlink.bus.api.system.dao;

import com.phlink.bus.api.system.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);

    /**
     * 查找当前菜单/按钮关联的用户 ID
     *
     * @param menuId menuId
     * @return 用户 ID集合
     */
    List<Long> findUserIdsByMenuId(Long menuId);

    /**
     * 递归删除菜单/按钮
     *
     * @param menuId menuId
     */
    void deleteMenus(Long menuId);
}