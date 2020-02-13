package com.phlink.bus.api.device.domain.VO;

import lombok.Data;

@Data
public class DeviceCountAll {
    private Integer bus;
    private Integer busRunning;
    private Integer ewatch;
    private Integer ewatchBind;
}
