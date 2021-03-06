/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:09:39
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:09:39
 */
package com.puhuilink.qbs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puhuilink.qbs.auth.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Permission> listByUserId(@Param("userId") String userId);
}
