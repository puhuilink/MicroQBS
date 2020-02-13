package com.phlink.bus.api.route.dao;

import com.phlink.bus.api.route.domain.TripLog;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zhouyi
 */
public interface TripLogMapper extends BaseMapper<TripLog> {
	
	TripLog detailNowTripLog(@Param("tripTime") String tripTime,@Param("busTeacherId") Long busTeacherId);
	
	TripLog checkNowTripLog(@Param("tripTime") String tripTime,@Param("busTeacherId") Long busTeacherId, @Param("state")String state);

	TripLog getByBusCode(@Param("busCode") String busCode);

    void incrTripLogUpNumber(@Param("tripLogId") Long tripLogId);

	void incrTripLogDownNumber(@Param("tripLogId") Long tripLogId);

	TripLog getByTripId(@Param("tripId") Long tripId, @Param("busTeacherId") Long teacherId);
}
