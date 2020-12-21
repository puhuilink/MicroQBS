/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:09:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:09:55
 */
package com.puhuilink.qbs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puhuilink.qbs.auth.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
