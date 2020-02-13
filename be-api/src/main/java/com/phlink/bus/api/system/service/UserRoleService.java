package com.phlink.bus.api.system.service;


import com.phlink.bus.api.system.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(Long[] roleIds);

	void deleteUserRolesByUserId(Long[] userIds);

	List<Long> findUserIdsByRoleId(Long[] roleIds);

    void deleteVisitorRole(Long userId);

	void addGuardianRole(Long userId);

    void deleteGuardianRole(Long userId);

	void addVisitorRole(Long userId);
}
