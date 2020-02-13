package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.BusWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface IBusWorkerService extends IService<BusWorker> {

    /**
    * 查询列表
    * @param request
    * @param busWorker
    * @return
    */
    IPage<BusWorker> listBusWorkers(QueryRequest request, BusWorker busWorker);

    /**
    * 新增
    * @param busWorker
    */
    void createBusWorker(BusWorker busWorker);

    /**
    * 修改
    * @param busWorker
    */
    void modifyBusWorker(BusWorker busWorker);

    /**
    * 批量删除
    * @param busWorkerIds
    */
    void deleteBusWorkerIds(String[] busWorkerIds);
    /**
     * 根据司乘人员获取到当前的车辆信息
     * @return
     */
    Bus findMyBus();
}
