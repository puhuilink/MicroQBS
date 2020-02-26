package com.phlink.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.User;
import com.phlink.core.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    User getByUsername(@Param("username") String username);
}