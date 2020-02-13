package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class BusTripTime {
    private Long routeId;
    private String routeName;
    private Long tripId;
    private Long schoolId;
    private TripRedirectEnum directionId;
    private LocalTime startTime;
    private LocalTime endTime;
}
