package com.phlink.bus.api.common.properties;

import lombok.Data;

@Data
public class IotServerProperties {

    private String url;
    private String username;
    private String password;
    private String cron;
    private String gateway;

}
