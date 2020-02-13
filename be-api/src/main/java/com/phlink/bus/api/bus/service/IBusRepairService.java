package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.BusRepair;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface IBusRepairService extends IService<BusRepair> {

    /**
    * 查询列表
    * @param request
    * @param busRepair
    * @return
    */
    IPage<BusRepair> listBusRepairs(QueryRequest request, BusRepair busRepair);

    /**
    * 新增
    * @param busRepair
    */
    void createBusRepair(BusRepair busRepair);

    /**
    * 修改
    * @param busRepair
    */
    void modifyBusRepair(BusRepair busRepair);

    /**
    * 批量删除
    * @param busRepairIds
    */
    void deleteBusRepairIds(String[] busRepairIds);
}
