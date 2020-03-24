package com.phlink.core.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.core.web.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    User getByUsername(@Param("username") String username);
}