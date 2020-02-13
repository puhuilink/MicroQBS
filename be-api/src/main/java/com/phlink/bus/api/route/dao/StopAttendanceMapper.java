package com.phlink.bus.api.route.dao;

import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.route.domain.StopAttendance;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalTime;

/**
 * @author wen
 */
public interface StopAttendanceMapper extends BaseMapper<StopAttendance> {
	
	StopAttendance  findIsAttendance(@Param("studentId")Long studentId,
			@Param("tripId")Long tripId,@Param("type")String type,@Param("stopId")Long stopId);

    BusTripTime getBusTripTimeByTime(@Param("time") LocalTime time, @Param("stopId") Long stopId);

	StopAttendance getTodayLastAttendanceByTripAndBus(@Param("studentId") Long studentId, @Param("tripId") Long tripId, @Param("busCode") String busCode);
}
