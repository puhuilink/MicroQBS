package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.UserRole;
import com.phlink.core.mapper.UserRoleMapper;
import com.phlink.core.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> listByUserId(String userId) {

        return userRoleMapper.listByUserId(userId);
    }

    @Override
    public List<String> listDepIdsByUserId(String userId) {

        return userRoleMapper.listDepIdsByUserId(userId);
    }
}