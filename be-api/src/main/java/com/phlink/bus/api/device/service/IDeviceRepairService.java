package com.phlink.bus.api.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.device.domain.DeviceRepair;

/**
 * @author zhouyi
 */
public interface IDeviceRepairService extends IService<DeviceRepair> {

/**
* 查询列表
* @param request
* @param deviceRepair
* @return
*/
IPage<DeviceRepair> listDeviceRepairs(QueryRequest request, DeviceRepair deviceRepair);

 /**
 * 新增
 * @param deviceRepair
 */
 void createDeviceRepair(DeviceRepair deviceRepair);
 /**
 * 修改
 * @param deviceRepair
 */
 void modifyDeviceRepair(DeviceRepair deviceRepair);

 /**
 * 批量删除
 * @param deviceRepairIds
 */
 void deleteDeviceRepairIds(String[] deviceRepairIds);
}
