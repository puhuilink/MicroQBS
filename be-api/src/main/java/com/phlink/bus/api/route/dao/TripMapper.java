package com.phlink.bus.api.route.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.Trip;
import com.phlink.bus.api.route.domain.TripStopTimeDetail;
import com.phlink.bus.api.route.domain.TripTime;
import com.phlink.bus.api.route.domain.vo.StopStudentVo;
import com.phlink.bus.api.route.domain.vo.TripStateVO;
import com.phlink.bus.api.route.domain.vo.TripStopStudentVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;
import java.util.List;

/**
 * @author wen
 */
public interface TripMapper extends BaseMapper<Trip> {
	/**
	 * 根据studentId查询出该学生的所有行程
	 */
	List<Trip> findTripListByStudentId(@Param("studentId") Long studentId);
	
	/**
	 * 根据studentId查询出该学生的所有行程出席
	 */
	List<TripStopStudentVO> findTripStopStudent(@Param("tripTime") String tripTime, @Param("busTeacherId") Long busTeacherId,
			@Param("inType") String inType,@Param("outType") String outType);
	
	/**
	 * 查询出当前老师的车辆的行程的状态
	 * @param tripId
	 * @param userId
     * @return
	 */
	List<TripStateVO> findTripState(@Param("tripId") Long tripId, @Param("userId")Long userId);
	
	/**
	 * 查询出当前随行老师所在车辆的某站点的学生出勤信息
	 * @param stopId
	 * @param tripTime
	 * @param busTeacherId
	 * @return
	 */
	List<StopStudentVo> findStuStateByStopId(@Param("stopId") Long stopId,@Param("tripTime") String tripTime,
			@Param("busTeacherId") Long busTeacherId,@Param("type") String type);
	
	/**
	 * 查询出当前随行老师所在车辆的学校站点的未出勤学生信息
	 * @param tripTime
	 * @param busTeacherId
	 * @return
	 */
	List<StopStudentVo> findSchoolStuState(@Param("tripTime") String tripTime,@Param("busTeacherId") Long busTeacherId,
			@Param("type") String type);
	
	TripStateVO findTripById(@Param("tripId")Long tripId);

    Trip getNowTripForStudent(@Param("studentId") Long studentId);

	Trip getNowTripForRouteOperation(@Param("routeOperationId") Long routeOperationId);
	
	Trip getRouteIdByTeacher(@Param("userId")Long userId);

    List<TripStopTimeDetail> listTripStopTimeDetail(@Param("now") LocalTime now, @Param("routeId") Long routeId, @Param("tripId") Long tripId);

    List<Trip> listTripByRoute(@Param("routeId") Long routeId);

    List<StopTimeStudentDetail> listStopTimeStudentDetailByTripId(@Param("tripId") Long tripId, @Param("directionId") Integer directionId);

    TripTime getTripTimeByTime(@Param("time") LocalTime time, @Param("busId") Long busId);

	List<TripTime> listTripTimeByRoute(@Param("routeId") Long routeId);
}
