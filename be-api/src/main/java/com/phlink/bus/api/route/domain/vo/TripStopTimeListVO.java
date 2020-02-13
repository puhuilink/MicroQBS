package com.phlink.bus.api.route.domain.vo;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import lombok.Data;


@Data
public class TripStopTimeListVO {
    private Long tripId;
    private TripRedirectEnum directionId;
    private TripTimeEnum tripTime;
    private JSON stopTimes;
}
