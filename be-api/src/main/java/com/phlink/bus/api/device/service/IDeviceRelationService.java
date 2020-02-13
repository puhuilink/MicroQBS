package com.phlink.bus.api.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.DeviceRelation;

/**
 * @author zhouyi
 */
public interface IDeviceRelationService extends IService<DeviceRelation> {

    /**
     * 查询列表
     *
     * @param request
     * @param deviceRelation
     * @return
     */
    IPage<DeviceRelation> listDeviceRelations(QueryRequest request, DeviceRelation deviceRelation);

    /**
     * 新增
     *
     * @param deviceRelation
     */
    void createDeviceRelation(DeviceRelation deviceRelation);

    /**
     * 修改
     *
     * @param deviceRelation
     */
    void modifyDeviceRelation(DeviceRelation deviceRelation);

    /**
     * 批量删除
     *
     * @param deviceRelationIds
     */
    void deleteDeviceRelationIds(String[] deviceRelationIds);

    /**
     * 根据deviceCode删除
     *
     * @param deviceCode
     */
    void deleteDeviceRelationByDeviceCode(String deviceCode) throws BusApiException;

    DeviceRelation getByDeviceCode(String deviceId);
}
