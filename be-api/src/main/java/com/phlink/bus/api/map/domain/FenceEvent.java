package com.phlink.bus.api.map.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FenceEvent {
    private String client_status;

    private String client_action;

    private String enter_time;

    private FenceInfo fence_info;
}
