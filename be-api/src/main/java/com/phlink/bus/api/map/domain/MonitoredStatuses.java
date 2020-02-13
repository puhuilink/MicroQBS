package com.phlink.bus.api.map.domain;

import lombok.Data;

@Data
public class MonitoredStatuses {
    private Integer fence_id;
    private String monitored_status;
}
