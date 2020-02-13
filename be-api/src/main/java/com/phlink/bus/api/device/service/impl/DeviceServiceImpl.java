package com.phlink.bus.api.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.device.dao.DeviceMapper;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.domain.DeviceRelation;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.domain.VO.BindingDeviceVO;
import com.phlink.bus.api.device.domain.VO.DeviceViewVO;
import com.phlink.bus.api.device.domain.enums.DeviceStatusEnum;
import com.phlink.bus.api.device.manager.IotManager;
import com.phlink.bus.api.device.service.IDeviceRelationService;
import com.phlink.bus.api.device.service.IDeviceService;
import com.phlink.bus.api.map.service.IMapAmapService;
import io.rpc.core.device.EWatchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zy
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private IDeviceRelationService deviceRelationService;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IotManager iotManager;

    /**
     * 查询设备列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<Device> listDevices(QueryRequest request, DeviceViewVO deviceViewVO) {
        Page<Device> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "d.id", BusApiConstant.ORDER_DESC, false);
        Page<Device> pageResult = this.baseMapper.listDevices(page, deviceViewVO);
        List<Device> devices = pageResult.getRecords();
        List<Device> devices2 = devices.stream().peek(d -> {
            EWatchInfo eWatchInfo = cacheDeviceInfoService.getLastEWatch(d.getDeviceCode());
            d.setLastLocation(new EwatchLocation(eWatchInfo));
        }).collect(Collectors.toList());
        pageResult.setRecords(devices2);
        return pageResult;
    }

    @Override
    public List<Device> listDevices(DeviceViewVO deviceViewVO) {
        Page<Device> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.baseMapper.listDevices(page, deviceViewVO);
        List<Device> devices = page.getRecords();
        List<Device> devices2 = devices.stream().peek(d -> {
            EWatchInfo eWatchInfo = cacheDeviceInfoService.getLastEWatch(d.getDeviceCode());
            d.setLastLocation(new EwatchLocation(eWatchInfo));
        }).collect(Collectors.toList());
        return devices2;
    }

    @Override
    @Transactional
    public void createDevice(Device device) throws BusApiException, RedisConnectException {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getDeviceCode, device.getDeviceCode());
        int deviceCodeCount = baseMapper.selectCount(queryWrapper);
        if(deviceCodeCount > 0) {
            throw new BusApiException("手环设备已存在");
        }
        device.setCreateTime(LocalDateTime.now());
        device.setDeviceStatus(DeviceStatusEnum.NORMAL);
        this.save(device);
        createToGaoDeMap(device);
    }

    @Override
    @Transactional
    public void modifyDevice(Device device) {
        device.setModifyTime(LocalDateTime.now());
        device.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(device);
    }

    @Override
    public void deleteDeviceIds(String[] deviceIds) throws BusApiException, RedisConnectException {
        List<Long> list = Stream.of(deviceIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        for (Long id : list) {
            Device device = this.getById(id);

            delFromGaoDeMap(device);
            //删除关系表
            this.deviceRelationService.deleteDeviceRelationByDeviceCode(device.getDeviceCode());
        }
        removeByIds(list);
    }

    @Override
    public Device findByDeviceCode(String deviceCode) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<Device>().eq(Device::getDeviceCode, deviceCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteByDeviceCode(String deviceCode) throws BusApiException {
        Device device = findByDeviceCode(deviceCode);
        if(device == null) {
            log.error("手环设备" + deviceCode + "不存在");
            return;
        }
        delFromGaoDeMap(device);
        this.removeById(device.getId());
    }

    @Override
    public Device getByStudentId(Long studentId) {
        return baseMapper.getByStudentId(studentId);
    }

    @Override
    public List<Device> listByStudentInfo(Long studentId, String studentName, String schoolName, String busCode, String numberPlate) {
        return baseMapper.listByStudentInfo(studentId, studentName, schoolName, busCode, numberPlate);
    }

    @Override
    public int countBindDevice() {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getBindingStatus, true);
        return this.count(queryWrapper);
    }

    @Override
    public void bindingDevice(BindingDeviceVO bindingDeviceVO) throws BusApiException {
        // 检查是否已绑定过手环
        LambdaQueryWrapper<DeviceRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceRelation::getStudentId, bindingDeviceVO.getStudentId());
        int bindCount = this.deviceRelationService.count(lambdaQueryWrapper);
        if (bindCount > 0) {
            throw new BusApiException("该孩子已绑定过手环，请解绑后再次绑定");
        }
        LambdaQueryWrapper<DeviceRelation> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(DeviceRelation::getDeviceCode, bindingDeviceVO.getDeviceCode());
        int bindCount2 = this.deviceRelationService.count(lambdaQueryWrapper2);
        if (bindCount2 > 0) {
            throw new BusApiException("该手环已绑定过，请解绑后再次绑定");
        }
        DeviceRelation deviceRelation = new DeviceRelation();
        deviceRelation.setCreateTime(LocalDateTime.now());
        deviceRelation.setDeviceCode(bindingDeviceVO.getDeviceCode());
        deviceRelation.setStudentId(bindingDeviceVO.getStudentId());
        deviceRelation.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.deviceRelationService.createDeviceRelation(deviceRelation);
        // 修改设备绑定状态
        UpdateWrapper<Device> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Device::getBindingStatus, true);
        wrapper.lambda().eq(Device::getDeviceCode, bindingDeviceVO.getDeviceCode());
        this.update(wrapper);
    }

    @Override
    public void unbindingDevice(BindingDeviceVO bindingDeviceVO) throws BusApiException {
        this.deviceRelationService.deleteDeviceRelationByDeviceCode(bindingDeviceVO.getDeviceCode());
        // 修改设备绑定状态
        UpdateWrapper<Device> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Device::getBindingStatus, false);
        wrapper.lambda().eq(Device::getDeviceCode, bindingDeviceVO.getDeviceCode());
        this.update(wrapper);
    }

    private void createToGaoDeMap(Device device) {
        Long tid = this.mapAmapService.createAmapEntity(device.getDeviceCode(), device.getId().toString());
        UpdateWrapper<Device> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Device::getTid, tid);
        wrapper.lambda().eq(Device::getDeviceCode, device.getDeviceCode());
        this.update(wrapper);
        try {
            this.redisService.hset(BusApiConstant.DEVICE, device.getDeviceCode(), String.valueOf(tid));
        } catch (RedisConnectException e) {
            log.error(device.getDeviceCode() + "加入缓存失败！");
        }
    }

    private void delFromGaoDeMap(Device device) {
        if(device == null) {
            return;
        }
        this.mapAmapService.deleteAmapEntity(device.getTid());
        try {
            this.redisService.hdel(BusApiConstant.DEVICE, device.getDeviceCode());
        } catch (RedisConnectException e) {
            log.error(device.getDeviceCode() + "删除缓存失败！");
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    @Override
    public void deviceHeartBeat() {
        List<Device> allDevice = this.list();
        LocalDateTime now = LocalDateTime.now();
        for(Device d : allDevice) {
            EWatchInfo eWatchInfo = cacheDeviceInfoService.getLastEWatch(d.getDeviceCode());
            if(eWatchInfo == null) {
                continue;
            }
            TimeZone chinaZone = TimeZone.getTimeZone("GMT+08:00");
            long timeStamp = eWatchInfo.getTimestamp();
            LocalDateTime lastLocalTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                            chinaZone.toZoneId());
            Duration duration = Duration.between(lastLocalTime, now);
            // 10分钟触发一次
            if(duration.getSeconds() > 10*60) {
                iotManager.cr(d.getDeviceCode());
            }
        }
    }

    @Override
    public void batchRegisterToGaode(List<Device> devices) {
        for(Device device : devices) {
            createToGaoDeMap(device);
        }
    }
}
