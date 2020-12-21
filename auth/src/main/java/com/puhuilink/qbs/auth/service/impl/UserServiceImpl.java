/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:00
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:46
 */
package com.puhuilink.qbs.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.auth.entity.Department;
import com.puhuilink.qbs.auth.entity.Permission;
import com.puhuilink.qbs.auth.entity.Role;
import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.mapper.DepartmentMapper;
import com.puhuilink.qbs.auth.mapper.PermissionMapper;
import com.puhuilink.qbs.auth.mapper.UserMapper;
import com.puhuilink.qbs.auth.mapper.UserRoleMapper;
import com.puhuilink.qbs.auth.security.model.TokenStatus;
import com.puhuilink.qbs.auth.service.UserService;
import com.puhuilink.qbs.auth.service.UserTokenService;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserTokenService userTokenService;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.last("limit 1");
        User user = this.getOne(queryWrapper);
        if (user == null) {
            return null;
        }
        // 关联部门
        if (StringUtils.isNotBlank(user.getDepartmentId())) {
            Department department = departmentMapper.selectById(user.getDepartmentId());
            if (department != null) {
                user.setDepartmentTitle(department.getTitle());
            }
        }
        // 关联角色
        List<Role> roleList = userRoleMapper.listByUserId(user.getId());
        user.setRoles(roleList);
        // 关联权限菜单
        List<Permission> permissionList = permissionMapper.listByUserId(user.getId());
        user.setPermissions(permissionList);
        return user;
    }

    @Override
    public User getByMobile(String mobile) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, mobile);
        queryWrapper.last("limit 1");
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User getByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        queryWrapper.last("limit 1");
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<User> listByCondition(User user, SearchVO searchVo) {
        return null;
    }

    @Override
    public List<User> listByDepartmentId(String departmentId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDepartmentId, departmentId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<User> listByUsernameLikeAndStatus(String username, Integer status) {
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public void resetPassword(String[] userIds) {
        String newPassword = new BCryptPasswordEncoder().encode("123456");
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(User::getId, userIds).set(User::getPassword, newPassword);
        this.update(updateWrapper);
        // 将token设置为reset
        userTokenService.updateLoginStatusToReset(userIds, TokenStatus.RESET);
    }
}
