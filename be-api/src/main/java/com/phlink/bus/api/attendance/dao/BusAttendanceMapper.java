package com.phlink.bus.api.attendance.dao;

import com.phlink.bus.api.attendance.domain.BusAttendance;
import com.phlink.bus.api.attendance.domain.vo.BusAttendanceVO;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ZHOUY
 */
public interface BusAttendanceMapper extends BaseMapper<BusAttendance> {
	
	List<BusAttendanceVO> selectListByCondi(BusAttendanceVO BusAttendance);
	
	BusAttendance findAttendanceInToday(@Param("userId")Long userId);
	BusAttendance findAttendanceInDay(@Param("userId")Long userId,@Param("time")LocalDate time);
}
