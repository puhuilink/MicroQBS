/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:09:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:09:55
 */
package com.phlink.qbs.core.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.qbs.core.web.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    User getByUsername(@Param("username") String username);
}
