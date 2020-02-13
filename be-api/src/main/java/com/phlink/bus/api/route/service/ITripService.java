package com.phlink.bus.api.route.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.*;
import com.phlink.bus.api.route.domain.vo.StopStudentVo;
import com.phlink.bus.api.route.domain.vo.TripStateVO;
import com.phlink.bus.api.route.domain.vo.TripStopStudentVO;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import org.redisson.api.RList;

import java.time.LocalTime;
import java.util.List;
/**
 * @author wen
 */
public interface ITripService extends IService<Trip> {


    /**
    * 获取详情
    */
    Trip findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param trip
    * @return
    */
    IPage<Trip> listTrips(QueryRequest request, Trip trip);

    /**
    * 新增
    * @param trip
    */
    void createTrip(Trip trip);
    
    /**
     * 根据学生的ID查询出该学生的所有行程
     * @param id
     * @return
     */
    List<Trip> listTripByStudentId(Long studentId);
    
    /**
     * 获得随车老师的行程及站点信息以及每一个站点学生的出勤人数
     * @return
     */
    List<TripStopStudentVO> listTripStopTimeStudentDetail(String tripTime, Long schoolId);
    
    /**
     * 获得当前随性老师的当天的行程的状态
     * @return
     * @param tripId
     * @param userId
     */
    List<TripStateVO> listByBusTeacherForNowTrip(Long tripId, Long userId);
    
    /**
     * 获得某站点学生的出勤信息
     * @param stopId
     * @param tripTime
     * @return
     */
    List<StopStudentVo> listStuByStopId(Long stopId, String tripTime);
    
    /**
     * 获得学校站点未出勤的学生信息
     */
    List<StopStudentVo> listSchoolStudentBy(String tripTime);

    /**
     * 查看当前学生所在行程
     * @param studentId
     * @return
     */
    Trip getNowTripForStudent(Long studentId);

    Trip getNowTripForRouteOperation(Long routeOperationId);

    TripStopTimeDetailVO getBusStopTime(TripState tripState) throws BusApiException;

    DvrLocation location(String dvrCode);

    /**
     * 根据路线删除行程
     * @param routeIds
     */
    void deleteTripsByRouteIds(List<Long> routeIds);

    List<TripStopTimeDetail> listTripStopTimeDetailByTime(LocalTime now);

    List<TripStopTimeDetail> listTripStopTimeDetailByTripId(Long tripId);

    List<StopTimeStudentDetail> listStopTimeStudentDetailByTripId(Long tripId, Integer directionId);

    TripLog start(Bus bus, Long tripId) throws BusApiException;

    TripLog startTripOnTripLog(String busCode, TripState trip) throws BusApiException;

    TripLog stopTripOnTripLog(String busCode, TripState tripState) throws BusApiException;

    TripLog stop(Bus bus, Long tripId) throws BusApiException;

    TripState getCurrentRunningTrip(Bus bus);

    RList<StudentGuardianInfo> listAllStudentsOnTrip(TripState tripRunning);

    TripState getTripState(Bus bus, Long tripId) throws BusApiException;

    TripState getCurrentRunningTrip(String busCode);

    TripState getCurrentLogRunningTrip(String busCode);

    TripState getLastestWaittingTrip(Bus bus);

    TripState getLastestFinishedTrip(Bus bus);

    List<TripState> listTripToday(Bus bus) throws BusApiException;

    List<TripState> listTripStatesAll(String busCode);

    List<Long> listLeaveStudentId(TripState tripState, List<Long> studentIds);

    List<Long> listAllStudentId(TripState tripState);

    RList<StudentGuardianInfo> getAllStudentList(String busCode, Long stopTimeId);

    RList<Long> getUpStudentList(TripState tripState, String busCode);

    RList<Long> getDownStudentList(TripState tripState, String busCode);

    void attendanceUp(TripState state, Long studentId, Long teacherId, Long reason) throws BusApiException;

    void attendanceDown(TripState state, Long studentId, Long teacherId) throws BusApiException;

    void faceAttendance(TripState state, Long studentId, Long teacherId) throws BusApiException;

    List<Trip> listTripByRouteId(Long routeId);

    List<TripTime> listTripTimeByRoute(Long routeId);

    boolean checkSchoolStop(TripState tripState, List<StopTimeStudentDetail> stopTimeDetails, StopTimeStudentDetail stopTimeStudentDetail);

    TripState getTripStateByIdAndBusCode(Long tripId, String busCode);
}
