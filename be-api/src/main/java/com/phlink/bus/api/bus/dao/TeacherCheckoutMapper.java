package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.TeacherCheckout;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author wen
 */
public interface TeacherCheckoutMapper extends BaseMapper<TeacherCheckout> {
	
	TeacherCheckout findIsCheckout(@Param("userId")Long userId);
	
	TeacherCheckout findIsCheckoutInTime(@Param("userId")Long userId,@Param("time")LocalDate time);

}
