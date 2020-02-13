package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import lombok.Data;

import java.util.List;

@Data
public class TripDetailVO {
    private Long tripId;
    private TripRedirectEnum directionId;
    private TripTimeEnum tripTime;
    private List<StopTime> stopTimes;
}
