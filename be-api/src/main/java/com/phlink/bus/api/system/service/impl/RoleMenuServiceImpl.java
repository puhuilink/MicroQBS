package com.phlink.bus.api.system.service.impl;

import com.phlink.bus.api.system.dao.RoleMenuMapper;
import com.phlink.bus.api.system.domain.RoleMenu;
import com.phlink.bus.api.system.service.RoleMenuServie;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuServie {

	@Override
	@Transactional
	public void deleteRoleMenusByRoleId(Long[] roleIds) {
		baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, roleIds));
	}

	@Override
	@Transactional
	public void deleteRoleMenusByMenuId(Long[] menuIds) {
		baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getMenuId, menuIds));
	}

	@Override
	public List<RoleMenu> getRoleMenusByRoleId(Long roleId) {
		return baseMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
	}

}
