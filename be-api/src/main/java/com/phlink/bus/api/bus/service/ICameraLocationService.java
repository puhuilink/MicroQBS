package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.CameraLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface ICameraLocationService extends IService<CameraLocation> {


    /**
    * 获取详情
    */
    CameraLocation findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param cameraLocation
    * @return
    */
    IPage<CameraLocation> listCameraLocations(QueryRequest request, CameraLocation cameraLocation);

    /**
    * 新增
    * @param cameraLocation
    */
    void createCameraLocation(CameraLocation cameraLocation);

    /**
    * 修改
    * @param cameraLocation
    */
    void modifyCameraLocation(CameraLocation cameraLocation);

    /**
    * 批量删除
    * @param cameraLocationIds
    */
    void deleteCameraLocations(String[] cameraLocationIds);
}
