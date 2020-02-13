package com.phlink.bus.api.map.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.map.domain.FenceStatus;
import com.phlink.bus.api.map.domain.Points;
import com.phlink.bus.api.map.response.AmapCoordinateResultEntity;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import com.phlink.bus.api.map.response.AmapResultEntity;
import com.phlink.bus.api.trajectory.domain.Trajectory;

import java.math.BigDecimal;
import java.util.List;

public interface IMapAmapService {

    /**
     * 创建高德地图设备id
     *
     * @param name 终端名称
     * @param desc 终端描述
     * @return
     * @throws BusApiException
     */
    Long createAmapEntity(String desc, String name);

    /**
     * 删除高德地图设备id
     *
     * @return
     * @throws BusApiException
     */
    void deleteAmapEntity(Long tid);

    Long getAmapEntity(String name);

    /**
     * 创建电子围栏
     *
     * @param fenceVO
     * @return
     * @throws BusApiException
     */
    String createFence(FenceVO fenceVO) throws BusApiException;

    /**
     * 更新电子围栏
     *
     * @param fenceVO
     * @return
     * @throws BusApiException
     */
    void updateFence(FenceVO fenceVO) throws BusApiException;

    AmapResultEntity getFence(String fenceId) throws BusApiException;

    /**
     * 删除电子围栏
     *
     * @param fenceId
     * @throws BusApiException
     */
    void deleteFence(String fenceId) throws BusApiException;

    /**
     * 启动电子围栏
     *
     * @param fenceId
     * @throws BusApiException
     */
    void runFence(String fenceId) throws BusApiException;

    /**
     * 停用电子围栏
     *
     * @param fenceId
     * @throws BusApiException
     */
    void stopFence(String fenceId) throws BusApiException;

    /*    *//**
     * 查询设备与附近的围栏交互状态
     *
     * @param deviceId
     * @param location
     * @throws BusApiException
     *//*
    void discoverFence(Long deviceId, String location) throws BusApiException;*/

    /**
     * 获取轨迹信息
     *
     * @param trajectory
     * @param isHavePoints 是否需要轨迹点
     * @throws BusApiException
     */
    void getTrajectory(Trajectory trajectory, Boolean isHavePoints) throws BusApiException;

    /**
     * 创建轨迹
     *
     * @param tid
     * @return
     * @throws BusApiException
     */
    Long createTrajectory(Long tid) throws BusApiException;

    /**
     * 删除轨迹
     *
     * @param tid
     * @param trid
     * @throws BusApiException
     */
    void deleteTrajectory(Long tid, Long trid) throws BusApiException;

    /**
     * 上传轨迹
     *
     * @param tid
     * @param trid
     * @param pointsList
     * @throws BusApiException
     */
    void uploadTrajectory(Long tid, Long trid, List<Points> pointsList) throws BusApiException;

    /**
     * 获取坐标围栏告警
     *
     * @param code
     * @param lon
     * @param lat
     * @param time
     * @throws BusApiException
     */
    FenceStatus getAlarmFence(String code, Double lon, Double lat, Long time) throws BusApiException;

    /**
     * 逆地理编码获得街道信息
     * @param longitude
     * @param latitude
     * @return
     */
    String getLocationRegeo(BigDecimal longitude, BigDecimal latitude) throws BusApiException;

    /**
     * 转换GPS坐标为高德坐标系
     * @param longitude
     * @param latitude
     * @return
     */
    AmapCoordinateResultEntity convertGpsCoordsys(String longitude, String latitude) throws BusApiException;

    AmapDistanceResultEntity getDistance(List<String> origins, String destination, String type) throws BusApiException;
}
