package com.phlink.bus.api.route.domain;

import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TripTime {
    private Long tripId;
    private Long routeId;
    private Long schoolId;
    private TripTimeEnum tripTime;
    private TripRedirectEnum directionId;
    private String routeName;
    private RouteTypeEnum routeType;
    private LocalTime startTime;
    private LocalTime endTime;
}
