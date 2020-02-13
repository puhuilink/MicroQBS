package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.DriverCheckout;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author wen
 */
public interface DriverCheckoutMapper extends BaseMapper<DriverCheckout> {
	DriverCheckout findIsCheckout(@Param("userId")Long userId);

	DriverCheckout findIsCheckoutInTime(@Param("userId")Long userId,@Param("time")LocalDate time);
}
