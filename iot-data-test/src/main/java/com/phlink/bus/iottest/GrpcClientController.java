package com.phlink.bus.iottest;

import io.rpc.core.device.BusInfo;
import io.rpc.core.device.EWatchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GrpcClientController {

    @Autowired
    private GrpcClientService grpcClientService;

    @PostMapping("/ewatch")
    public String ewatch(@RequestParam(defaultValue = "1234567890") String deviceId,
                         @RequestParam(defaultValue = "100") Integer batteryLevel,
                         @RequestParam(defaultValue = "120") Integer direction,
                         @RequestParam(defaultValue = "1.000002") Double latitude,
                         @RequestParam(defaultValue = "2.000003") Double longitude,
                         @RequestParam(defaultValue = "60") Integer speed
                         ) {
        EWatchInfo eWatchInfo = EWatchInfo.newBuilder()
                .setBatteryLevel(batteryLevel)
                .setDeviceId(deviceId)
                .setDirection(direction)
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setSpeed(speed)
                .setTimestamp(System.currentTimeMillis())
                .build();
        return grpcClientService.saveEWatch(eWatchInfo);
    }

    @PostMapping("/bus")
    public String bus(@RequestParam(defaultValue = "9876543210") String busId,
                         @RequestParam(defaultValue = "120") Integer direction,
                         @RequestParam(defaultValue = "1.000002") Double latitude,
                         @RequestParam(defaultValue = "2.000003") Double longitude,
                         @RequestParam(defaultValue = "60") Integer speed
                         ) {
        BusInfo busInfo = BusInfo.newBuilder()
                .setBusId(busId)
                .setDirection(direction)
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setSpeed(speed)
                .setTimestamp(System.currentTimeMillis())
                .build();
        return grpcClientService.saveBus(busInfo);
    }

}