/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:00
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:46
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import com.puhuilink.qbs.core.web.controller.vo.UserData;
import com.puhuilink.qbs.core.web.entity.Department;
import com.puhuilink.qbs.core.web.entity.Permission;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.entity.User;
import com.puhuilink.qbs.core.web.mapper.DepartmentMapper;
import com.puhuilink.qbs.core.web.mapper.PermissionMapper;
import com.puhuilink.qbs.core.web.mapper.UserMapper;
import com.puhuilink.qbs.core.web.mapper.UserRoleMapper;
import com.puhuilink.qbs.core.web.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User getByUsername(String username) {
        User user = baseMapper.getByUsername(username);
        if (user == null) {
            return null;
        }
        // 关联部门
        if (StrUtil.isNotBlank(user.getDepartmentId())) {
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

    @Override
    public List<UserData> listUserData() {
        List<User> users = list();

        return users.stream().map(u -> {
            UserData ud = new UserData();
            BeanUtil.copyProperties(u, ud);
            return ud;
        }).collect(Collectors.toList());
    }

    @Override
    public void saveBatch(List<UserData> dataList) {
        List<User> users = dataList.stream().map(ud -> {
            User u = new User();
            BeanUtil.copyProperties(ud, u);
            return u;
        }).collect(Collectors.toList());
        saveBatch(users);
    }
}
