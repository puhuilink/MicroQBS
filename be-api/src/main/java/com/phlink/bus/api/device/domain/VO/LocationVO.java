package com.phlink.bus.api.device.domain.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationVO {
    private String deviceId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer direction;
}
