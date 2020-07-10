/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:54
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:40
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.mapper.RoleMapper;
import com.puhuilink.qbs.core.web.service.RoleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<Role> listByDefaultRole(Boolean defaultRole) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getDefaultRole, true);
        return this.list(queryWrapper);
    }
}
