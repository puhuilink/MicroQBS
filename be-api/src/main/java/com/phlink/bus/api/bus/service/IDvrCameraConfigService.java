package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.CameraLocation;
import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.bus.domain.VO.DvrBusLocationInfoVO;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;

import java.util.List;

/**
 * @author wen
 */
public interface IDvrCameraConfigService extends IService<DvrCameraConfig> {
    
    /**
    * 获取详情
    */
    DvrCameraConfig findById(Long id);

    /**
    * 新增
    * @param dvrCameraConfig
    */
    void createDvrCameraConfig(DvrCameraConfig dvrCameraConfig) throws BusApiException;

    /**
    * 修改
    * @param dvrCameraConfig
    */
    void modifyDvrCameraConfig(DvrCameraConfig dvrCameraConfig);

    /**
    * 批量删除
    * @param dvrCameraConfigIds
    */
    void deleteDvrCameraConfigs(String[] dvrCameraConfigIds);

    /**
     * 根据车辆ID获取dvr的安装列表
     * @param busId
     * @return
     */
    List<DvrBusLocationInfoVO> listDvrBusLocationInfo(Long busId);

    /**
     * 家长可看的dvr摄像头信息
     * @param busId
     * @return
     */
    DvrBusLocationInfoVO getDvrBusLocationInfoByGuardian(Long busId);

    /**
     * 设备闲置的通道
     * @param dvrCode
     * @return
     */
    List<Integer> listChannelCodeIdle(String dvrCode) throws BusApiException;

    /**
     * 闲置的摄像头位置
     * @param dvrCode
     * @return
     */
    List<CameraLocation> listCameraLocationIdle(String dvrCode) throws BusApiException;

    /**
     * 根据车辆ID获取配置列表
     * @param busId
     * @return
     */
    List<DvrCameraConfig> listByBusId(Long busId);

    /**
     * 获得家长视角的dvr配置
     * @param busId
     * @return
     */
    DvrCameraConfig getGuardianLocationConfig(Long busId);
}
