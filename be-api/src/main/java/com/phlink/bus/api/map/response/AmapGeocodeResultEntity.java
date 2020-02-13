package com.phlink.bus.api.map.response;

import lombok.Data;

import java.util.Map;

@Data
public class AmapGeocodeResultEntity {
    private String status;
    private String info;
    private Map<String, Object> regeocode;
    private String infocode;

    public boolean requestSuccess() {
        return "1".equals(status) && "10000".equals(infocode);
    }
}
