/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:09:58
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:09:58
 */
package com.phlink.qbs.core.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.qbs.core.web.entity.Role;
import com.phlink.qbs.core.web.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Role> listByUserId(@Param("userId") String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> listDepIdsByUserId(@Param("userId") String userId);
}
