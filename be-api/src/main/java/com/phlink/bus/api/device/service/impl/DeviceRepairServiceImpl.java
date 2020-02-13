package com.phlink.bus.api.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.device.dao.DeviceRepairMapper;
import com.phlink.bus.api.device.domain.DeviceRepair;
import com.phlink.bus.api.device.service.IDeviceRepairService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Service
public class DeviceRepairServiceImpl extends ServiceImpl<DeviceRepairMapper, DeviceRepair> implements IDeviceRepairService {

    @Override
    public IPage<DeviceRepair> listDeviceRepairs(QueryRequest request, DeviceRepair deviceRepair) {
        QueryWrapper<DeviceRepair> queryWrapper = new QueryWrapper<>();

        //TODO:查询条件
        if (deviceRepair.getCreateTimeFrom() != null)
            queryWrapper.lambda().ge(DeviceRepair::getCreateTime, deviceRepair.getCreateTimeFrom());
        if (deviceRepair.getCreateTimeTo() != null)
            queryWrapper.lambda().le(DeviceRepair::getCreateTime, deviceRepair.getCreateTimeTo());
        Page<DeviceRepair> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createDeviceRepair(DeviceRepair deviceRepair) {
        deviceRepair.setCreateTime(LocalDateTime.now());
        this.save(deviceRepair);
    }

    @Override
    @Transactional
    public void modifyDeviceRepair(DeviceRepair deviceRepair) {
        deviceRepair.setModifyTime(LocalDateTime.now());
        deviceRepair.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(deviceRepair);
    }

    @Override
    public void deleteDeviceRepairIds(String[] deviceRepairIds) {
        List<Long> list = Stream.of(deviceRepairIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }
}
