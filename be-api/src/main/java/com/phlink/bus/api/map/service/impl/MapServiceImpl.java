package com.phlink.bus.api.map.service.impl;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.map.service.IMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapServiceImpl implements IMapService {

    @Autowired
    private IMapAmapService amapService;

    @Autowired
    private IMapBaiduService baiduService;

    @Override
    public void batchCreateBusEntity(List<Bus> list) throws BusApiException {
        String message = null;
        List<String> baiduExceptionList = new ArrayList<>();
        list.forEach(bus -> {
            this.amapService.createAmapEntity(bus.getNumberPlate(), bus.getId().toString());
            try {
                this.baiduService.createBaiduEntity(bus.getNumberPlate(), bus.getBusCode());
            } catch (Exception e) {
                baiduExceptionList.add(bus.getNumberPlate());
            }
        });
        if (!baiduExceptionList.isEmpty()) {
            message = "百度终端创建失败：" + String.join(",", baiduExceptionList) + ";" + message;
        }
        if (StringUtils.isNotBlank(message)) {
            throw new BusApiException(message);
        }
    }

    @Override
    public void batchCreateDeviceEntity(List<Device> list){
        list.forEach(device -> {
            this.amapService.createAmapEntity(device.getDeviceCode(), device.getId().toString());
        });
    }

}
