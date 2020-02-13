package com.phlink.bus.api.device.service;

import com.phlink.bus.api.device.domain.EwatchLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import io.rpc.core.device.EWatchInfo;

/**
 * @author wen
 */
public interface IEwatchLocationService extends IService<EwatchLocation> {


    /**
    * 获取详情
    */
    EwatchLocation findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param ewatchLocation
    * @return
    */
    IPage<EwatchLocation> listEwatchLocations(QueryRequest request, EwatchLocation ewatchLocation);

    /**
    * 新增
     * @param eWatchInfo
     */
    EwatchLocation createEwatchLocation(EWatchInfo eWatchInfo);

    /**
    * 修改
    * @param ewatchLocation
    */
    void modifyEwatchLocation(EwatchLocation ewatchLocation);

    /**
    * 批量删除
    * @param ewatchLocationIds
    */
    void deleteEwatchLocations(String[] ewatchLocationIds);
}
