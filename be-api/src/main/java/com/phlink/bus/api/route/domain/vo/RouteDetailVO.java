package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class RouteDetailVO {
    private Long routeId;
    private String routeName;
    private String routeDesc;
    private RouteTypeEnum routeType;
    private Long schoolId;
    private Long trajectoryId;
    private LocalTime outboundTime;
    private List<Stop> stops;
    private List<TripDetailVO> trips;
}
