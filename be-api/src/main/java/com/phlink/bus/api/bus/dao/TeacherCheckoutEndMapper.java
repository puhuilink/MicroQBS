package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.TeacherCheckoutEnd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * @author wen
 */
public interface TeacherCheckoutEndMapper extends BaseMapper<TeacherCheckoutEnd> {

    TeacherCheckoutEnd getCheckoutByDay(@Param("userId") Long userId, @Param("day") LocalDate day);
}
