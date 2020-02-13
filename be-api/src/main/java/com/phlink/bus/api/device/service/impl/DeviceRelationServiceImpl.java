package com.phlink.bus.api.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.device.dao.DeviceRelationMapper;
import com.phlink.bus.api.device.domain.DeviceRelation;
import com.phlink.bus.api.device.service.IDeviceRelationService;
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
public class DeviceRelationServiceImpl extends ServiceImpl<DeviceRelationMapper, DeviceRelation> implements IDeviceRelationService {

    @Override
    public IPage<DeviceRelation> listDeviceRelations(QueryRequest request, DeviceRelation deviceRelation) {
        QueryWrapper<DeviceRelation> queryWrapper = new QueryWrapper<>();

        //TODO:查询条件
        if (deviceRelation.getCreateTimeFrom() != null)
            queryWrapper.lambda().ge(DeviceRelation::getCreateTime, deviceRelation.getCreateTimeFrom());
        if (deviceRelation.getCreateTimeTo() != null)
            queryWrapper.lambda().le(DeviceRelation::getCreateTime, deviceRelation.getCreateTimeTo());
        Page<DeviceRelation> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createDeviceRelation(DeviceRelation deviceRelation) {
        deviceRelation.setCreateTime(LocalDateTime.now());
        this.save(deviceRelation);
    }

    @Override
    @Transactional
    public void modifyDeviceRelation(DeviceRelation deviceRelation) {
        deviceRelation.setModifyTime(LocalDateTime.now());
        deviceRelation.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(deviceRelation);
    }

    @Override
    public void deleteDeviceRelationIds(String[] deviceRelationIds) {
        List<Long> list = Stream.of(deviceRelationIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public void deleteDeviceRelationByDeviceCode(String deviceCode) throws BusApiException {
        // 不需要删除设备
//        this.deviceService.deleteByDeviceCode(deviceCode);
        UpdateWrapper<DeviceRelation> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DeviceRelation::getDeviceCode, deviceCode);
        wrapper.lambda().set(DeviceRelation::getDeleted, true);
        wrapper.lambda().set(DeviceRelation::getModifyBy, BusApiUtil.getCurrentUser().getUserId());
        wrapper.lambda().set(DeviceRelation::getModifyTime, LocalDateTime.now());
        this.update(wrapper);
    }

    @Override
    public DeviceRelation getByDeviceCode(String deviceCode) {
        return baseMapper.getByDeviceCode(deviceCode);
    }
}
