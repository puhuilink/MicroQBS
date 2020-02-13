package com.phlink.bus.api.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.system.dao.UserRoleMapper;
import com.phlink.bus.api.system.domain.UserRole;
import com.phlink.bus.api.system.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

	@Override
	@Transactional
	public void deleteUserRolesByRoleId(Long[] roleIds) {
		Arrays.stream(roleIds).forEach(id -> baseMapper.deleteByRoleId(id));
	}

	@Override
	@Transactional
	public void deleteUserRolesByUserId(Long[] userIds) {
		Arrays.stream(userIds).forEach(id -> baseMapper.deleteByUserId(id));
	}

	@Override
	public List<Long> findUserIdsByRoleId(Long[] roleIds) {

		List<UserRole> list = baseMapper.selectList(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, roleIds));
		return list.stream().map(UserRole::getUserId).collect(Collectors.toList());
	}

	@Override
	public void deleteVisitorRole(Long userId) {
		this.baseMapper.deleteUserRole(userId, BusApiConstant.ROLE_VISTOR);
	}

	@Override
	public void addGuardianRole(Long userId) {
		LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(UserRole::getRoleId, BusApiConstant.ROLE_MAINGUARDIAN);
		queryWrapper.eq(UserRole::getUserId, userId);
		int count = this.count(queryWrapper);
		if(count == 0) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(BusApiConstant.ROLE_MAINGUARDIAN);
			this.save(ur);
		}
	}

	@Override
	public void deleteGuardianRole(Long userId) {
		this.baseMapper.deleteUserRole(userId, BusApiConstant.ROLE_MAINGUARDIAN);
	}

	@Override
	public void addVisitorRole(Long userId) {
		LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(UserRole::getRoleId, BusApiConstant.ROLE_VISTOR);
		queryWrapper.eq(UserRole::getUserId, userId);
		int count = this.count(queryWrapper);
		if(count == 0) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(BusApiConstant.ROLE_VISTOR);
			this.save(ur);
		}
	}

}
