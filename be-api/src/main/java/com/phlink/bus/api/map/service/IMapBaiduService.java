package com.phlink.bus.api.map.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.map.response.BaiduFenceListResultEntity;
import com.phlink.bus.api.map.response.BaiduLocationStatusResultEntity;

import java.util.List;

public interface IMapBaiduService {

    /**
     * 创建百度地图设备id
     *
     * @param busCode     车架号
     * @param numberPlate 车牌号
     */
    boolean createBaiduEntity(String numberPlate, String busCode);

    /**
     * 删除百度地图设备
     *
     * @param entityName 车辆entityName
     */
    void deleteBaiduEntitys(String entityName);

    /**
     * 创建线型电子围栏
     *
     * @param fenceVO 电子围栏对象
     */
    int createpolylinefence(FenceVO fenceVO) throws BusApiException;

    /**
     * 修改线型电子围栏
     *
     * @param fenceVO
     * @throws BusApiException
     */
    void updatepolylinefence(FenceVO fenceVO) throws BusApiException;

    /**
     * 删除电子围栏
     *
     * @param fenceIds 百度电子围栏ids
     * @throws BusApiException
     */
    void deletepolylinefence(List<Integer> fenceIds) throws BusApiException;

    /*    *//**
     * 添加围栏监控设备
     *
     * @param entitys
     * @throws BusApiException
     *//*
    void addEntitys(String[] entitys, String fenceId) throws BusApiException;

    *//**
     * 删除围栏监控设备
     *
     * @param entitys
     * @throws BusApiException
     *//*
    void deleteEntitys(String[] entitys, String fenceId) throws BusApiException;*/

    /**
     * 查询路线围栏报警（百度）
     *
     * @param entityName 设备编码（百度）
     * @param lon        经度
     * @param lat        纬度
     * @throws BusApiException
     * @return
     */
    BaiduLocationStatusResultEntity queryLocationStatus(String entityName, List<String> singletonList, String lon, String lat);

    /**
     * 根据围栏ID获取百度围栏
     * @param fenceId
     * @return
     */
    BaiduFenceListResultEntity getFence(String fenceId);

}
