package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.DvrCameraConfigMapper;
import com.phlink.bus.api.bus.domain.CameraLocation;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.phlink.bus.api.bus.domain.VO.DvrBusLocationInfoVO;
import com.phlink.bus.api.bus.service.ICameraLocationService;
import com.phlink.bus.api.bus.service.IDvrCameraConfigService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.exception.BusApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class DvrCameraConfigServiceImpl extends ServiceImpl<DvrCameraConfigMapper, DvrCameraConfig> implements IDvrCameraConfigService {

    @Autowired
    private IDvrService dvrService;
    @Autowired
    private ICameraLocationService cameraLocationService;

    @Override
    public DvrCameraConfig findById(Long id){
        return this.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createDvrCameraConfig(DvrCameraConfig dvrCameraConfig) throws BusApiException {
        // 不能在同一个位置重复添加
        DvrCameraConfig checkChannel = getDvrCameraConfigByChannel(dvrCameraConfig);
        if(checkChannel != null) {
            throw new BusApiException("该通道已绑定");
        }
        DvrCameraConfig checkLocation = getDvrCameraConfigByLocation(dvrCameraConfig);
        if(checkLocation != null) {
            throw new BusApiException("该位置已绑定");
        }
        this.save(dvrCameraConfig);
        Dvr dvr = dvrService.getByDvrCode(dvrCameraConfig.getDvrCode());
        if(dvr.getDvrServerId() == null) {
            dvr.setDvrServerId(dvrCameraConfig.getDvrServerId());
            dvrService.modifyDvr(dvr);
        }
    }

    private DvrCameraConfig getDvrCameraConfigByChannel(DvrCameraConfig dvrCameraConfig) {
        LambdaQueryWrapper<DvrCameraConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrCameraConfig::getDvrCode, dvrCameraConfig.getDvrCode());
        queryWrapper.eq(DvrCameraConfig::getChannelCode, dvrCameraConfig.getChannelCode());
        return baseMapper.selectOne(queryWrapper);
    }


    private DvrCameraConfig getDvrCameraConfigByLocation(DvrCameraConfig dvrCameraConfig) {
        LambdaQueryWrapper<DvrCameraConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrCameraConfig::getDvrCode, dvrCameraConfig.getDvrCode());
        queryWrapper.eq(DvrCameraConfig::getLocationCode, dvrCameraConfig.getLocationCode());
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyDvrCameraConfig(DvrCameraConfig dvrCameraConfig) {
        this.updateById(dvrCameraConfig);
        Dvr dvr = dvrService.getByDvrCode(dvrCameraConfig.getDvrCode());
        if(dvr != null) {
            dvr.setDvrServerId(dvrCameraConfig.getDvrServerId());
            dvrService.modifyDvr(dvr);
        }
    }

    @Override
    public void deleteDvrCameraConfigs(String[] dvrCameraConfigIds) {
        List<Long> list = Stream.of(dvrCameraConfigIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<DvrBusLocationInfoVO> listDvrBusLocationInfo(Long busId) {
        return baseMapper.listDvrBusLocationInfo(busId);
    }

    @Override
    public DvrBusLocationInfoVO getDvrBusLocationInfoByGuardian(Long busId) {
        return baseMapper.getDvrBusLocationInfoByGuardian(busId);
    }

    @Override
    public List<Integer> listChannelCodeIdle(String dvrCode) throws BusApiException {
        Dvr dvr = dvrService.getByDvrCode(dvrCode);
        if(dvr == null) {
            throw new BusApiException("DVR 设备不存在");
        }
        Integer channelNumber = dvr.getChannelNumber();
        List<Integer> channel = new ArrayList<>();
        for(int i = 0; i < channelNumber; i++) {
            channel.add(i);
        }

        List<DvrBusLocationInfoVO> locationList = listDvrBusLocationInfo(dvr.getBusId());
        List<Integer> usedCode = locationList.stream().map(DvrBusLocationInfoVO::getChannelCode).collect(Collectors.toList());
        channel.removeAll(usedCode);
        return channel;
    }

    @Override
    public List<CameraLocation> listCameraLocationIdle(String dvrCode) throws BusApiException {
        Dvr dvr = dvrService.getByDvrCode(dvrCode);
        if(dvr == null) {
            throw new BusApiException("DVR 设备不存在");
        }
        List<CameraLocation> cameraConfigs = cameraLocationService.list();

        List<DvrBusLocationInfoVO> locationList = listDvrBusLocationInfo(dvr.getBusId());
        List<String> usedCode = locationList.stream().map(DvrBusLocationInfoVO::getLocationCode).collect(Collectors.toList());

        return cameraConfigs.stream().filter(location -> {
            if(usedCode.contains(location.getLocationCode())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DvrCameraConfig> listByBusId(Long busId) {
        LambdaQueryWrapper<DvrCameraConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrCameraConfig::getBusId, busId);
        queryWrapper.eq(DvrCameraConfig::getDeleted, false);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public DvrCameraConfig getGuardianLocationConfig(Long busId) {

        return baseMapper.getGuardianLocationConfig(busId);
    }
}
