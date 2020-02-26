package com.phlink.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.core.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Permission> listByUserId(@Param("userId") String userId);
}