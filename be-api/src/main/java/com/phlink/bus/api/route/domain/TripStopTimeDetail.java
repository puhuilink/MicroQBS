package com.phlink.bus.api.route.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @author wen
 */
@Data
public class TripStopTimeDetail implements Serializable {
    private Long id;
    private Long stopId;
    private LocalTime arrivalTime;
    private Integer stopSequence;
    private BigDecimal stopLon;
    private BigDecimal stopLat;
    private String stopName;
    private Long routeId;
    private String routeName;
    private Long tripId;
    private LocalTime tripStartTime;
    private LocalTime tripEndTime;
}
