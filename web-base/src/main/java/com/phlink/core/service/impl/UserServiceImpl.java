package com.phlink.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.common.vo.SearchVO;
import com.phlink.core.entity.*;
import com.phlink.core.mapper.DepartmentMapper;
import com.phlink.core.mapper.PermissionMapper;
import com.phlink.core.mapper.UserMapper;
import com.phlink.core.mapper.UserRoleMapper;
import com.phlink.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        if(user==null){
            return null;
        }
        // 关联部门
        if(StrUtil.isNotBlank(user.getDepartmentId())){
            Department department = departmentMapper.selectById(user.getDepartmentId());
            if(department!=null){
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
}