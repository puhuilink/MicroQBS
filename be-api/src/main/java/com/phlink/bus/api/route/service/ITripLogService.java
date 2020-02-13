package com.phlink.bus.api.route.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;

import java.util.List;

/**
 * @author zhouyi
 */
public interface ITripLogService extends IService<TripLog> {


    /**
    * 获取详情
    */
    TripLog findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param tripLog
    * @return
    */
    IPage<TripLog> listTripLogs(QueryRequest request, TripLog tripLog);

    TripLog startTripState(TripState tripState, Long busTeacherId, int leaveNum, int studentNum) throws BusApiException;

    TripLog stopTripState(TripState tripState, int leavenum) throws BusApiException;

    /**
    * 批量删除
    * @param tripLogIds
    */
    void deleteTripLogs(String[] tripLogIds);
   
    /**
     * 随车老师查看当前的行程状态
     * @param tripTime
     * @return
     */
    @Deprecated
    TripLog detailNowTripLog(String tripTime);
    @Deprecated
    TripLog detailNowTripLog(String tripTime, Long teacherId);
    @Deprecated
    List<TripLog> listTripLogNotEnd(Long busTeacherId);

    /**
     * 根据busCode获取tripLog
     * @param busCode
     * @return
     */
    TripLog getByBusCode(String busCode);

    void incrTripLogUpNumber(Long tripLogId);

    void incrTripLogDownNumber(Long tripLogId);

    TripLog getByTripId(Long tripId, Long userId);
}
