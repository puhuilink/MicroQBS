package com.phlink.bus.api.route.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * @author wen
 */
@Data
public class TripStopTimeDetailVO implements Serializable {
    private Long stopId;
    private LocalTime arrivalTime;
    private String stopName;

    private Long nextStopId;
    private String nextStopName;
    private Integer nextStopDistance;
    private Integer nextStopDuration;

    private Long routeId;
    private String routeName;
    private Long tripId;
    private LocalTime tripStartTime;
    private LocalTime tripEndTime;
}
