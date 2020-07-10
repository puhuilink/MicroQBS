/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:57
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:43
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.entity.UserRole;
import com.puhuilink.qbs.core.web.mapper.UserRoleMapper;
import com.puhuilink.qbs.core.web.service.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
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
