package com.phlink.bus.api.map.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FenceStatus {
    private List<FenceEvent> fencing_event_list;
    private Integer status;
    private String nearest_fence_distance;
    private String nearest_fence_gid;
}
