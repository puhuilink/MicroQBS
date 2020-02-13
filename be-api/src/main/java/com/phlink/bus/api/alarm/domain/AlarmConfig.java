package com.phlink.bus.api.alarm.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alarm-config")
public class AlarmConfig {
    private Long pexpire;
    private Double speed;
    private Integer offlineMinute;
    private Integer speedTimeUpMinute;
    private Integer routeOffsetDistance;
    private Integer routeOffsetDistanceUp;
    private Integer stopDistance;
    private Integer stopTimeMinute;
    private Integer offlineTimeMinute;
}
