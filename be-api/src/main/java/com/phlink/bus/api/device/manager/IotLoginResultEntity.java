package com.phlink.bus.api.device.manager;

import lombok.Data;

@Data
public class IotLoginResultEntity {
    private String token;
    private String refreshToken;
}
