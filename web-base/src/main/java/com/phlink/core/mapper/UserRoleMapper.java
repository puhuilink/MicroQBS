package com.phlink.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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