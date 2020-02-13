package com.phlink.bus.api.route.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.domain.vo.TripStopTimeListVO;

import java.util.List;

/**
 * @author wen
 */
public interface IStopTimeService extends IService<StopTime> {


    /**
     * 获取详情
     */
    StopTime findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param stopTime
     * @return
     */
    IPage<StopTime> listStopTimes(QueryRequest request, StopTime stopTime);

    /**
     * 新增
     *
     * @param stopTime
     */
    void createStopTime(StopTime stopTime) throws BusApiException;

    /**
     * 批量创建
     *
     * @param stopTime
     */
    void batchCreateStopTime(List<StopTime> stopTime) throws BusApiException;

    /**
     * 修改
     *
     * @param stopTime
     */
    void modifyStopTime(StopTime stopTime);

    /**
     * 批量修改
     *
     * @param stopTimeList
     */
    void batchModifyStopTime(List<StopTime> stopTimeList) throws BusApiException;

    /**
     * 批量删除
     *
     * @param stopTimeIds
     */
    void deleteStopTimes(String[] stopTimeIds);

    /**
     * 路线下的行程及行程站点
     *
     * @param routeId
     * @return
     */
    List<TripStopTimeListVO> listTripStopTimeListVO(Long routeId);

    List<StopTime> listByTrip(Long tripId, List<Long> stopIds);

    /**
     * 根据tripId获取stopTime列表
     *
     * @param tripId
     * @return
     */
    List<StopTime> listByTrip(Long tripId);

    /**
     * 获得随车老师的行程及站点信息
     *
     * @return
     */
    List<TripStopTimeListVO> listTripStopTimeListVOByBusTeacher();

    /**
     * 根据站点电子围栏id获取站点时刻
     *
     * @param stopFenceId
     * @return
     */
    StopTime getStopTimeByFenceId(String stopFenceId);

    /**
     * 获得行程第一站的站点名称
     * @param tripId
     * @return
     */
    StopTime getFirstStopOnTrip(Long tripId);

    /**
     * 获得行程的下一站
     * @param stopId
     * @param tripId
     * @return
     */
    StopTime getNext(Long stopId, Long tripId);

    void removeByStopId(List<Long> deleteStopIds);
}
