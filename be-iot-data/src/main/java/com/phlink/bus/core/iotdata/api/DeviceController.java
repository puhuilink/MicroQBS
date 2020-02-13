package com.phlink.bus.core.iotdata.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.phlink.bus.core.iotdata.domain.BusLocation;
import com.phlink.bus.core.iotdata.domain.EWatchLocation;
import com.phlink.bus.core.iotdata.service.DeviceInfoService;
import io.rpc.core.device.BusInfo;
import io.rpc.core.device.EWatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Validated
@RestController
@RequestMapping("device")
public class DeviceController {
    @Autowired
    private DeviceInfoService deviceInfoService;

    @GetMapping("/ewatch-info/{deviceId}")
    public EWatchLocation getEwacthLocationInfo(@PathVariable String deviceId) {
        EWatchInfo eWatchInfo = deviceInfoService.getLastEWatch(deviceId);
        return new EWatchLocation(eWatchInfo);
    }

    @GetMapping("/ewatch-list/{deviceId}")
    public List<EWatchLocation> listEwacthLocationInfo(@PathVariable String deviceId, @RequestParam Long startTimestamp, @RequestParam Long endTimestamp) {
        Collection<EWatchInfo> eWatchInfoList = deviceInfoService.listDeviceInfo(startTimestamp, endTimestamp, deviceId);
        return eWatchInfoList.stream().map(EWatchLocation::new).collect(Collectors.toList());
    }

    @GetMapping("/bus-info/{busId}")
    public BusLocation getBusLocationInfo(@PathVariable String busId) throws InvalidProtocolBufferException {
        BusInfo entity = deviceInfoService.getLastBus(busId);
        return new BusLocation(entity);
    }

    @GetMapping("/bus-list/{busId}")
    public List<BusLocation> listBusLocationInfo(@PathVariable String busId, @RequestParam Long startTimestamp, @RequestParam Long endTimestamp) throws InvalidProtocolBufferException {
        Collection<BusInfo> busInfoList = deviceInfoService.listBusInfo(startTimestamp, endTimestamp, busId);
        return busInfoList.stream().map(BusLocation::new).collect(Collectors.toList());
    }

}
