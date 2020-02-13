package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.DriverCheckoutEnd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * @author wen
 */
public interface DriverCheckoutEndMapper extends BaseMapper<DriverCheckoutEnd> {

    DriverCheckoutEnd getCheckoutByDay(@Param("userId") Long userId, @Param("day") LocalDate day);
}
