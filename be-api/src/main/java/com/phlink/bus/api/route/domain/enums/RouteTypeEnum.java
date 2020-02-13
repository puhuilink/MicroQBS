package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import com.phlink.bus.api.common.exception.BusApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public enum RouteTypeEnum implements IEnum<String> {

    // 校车接孩子
    ONE_WAY("1", "单程", Collections.singletonList(TripTimeEnum.MORNING_GO)),
    // 校车送孩子回家
    ROUND_TRIP("2", "往返", Arrays.asList(TripTimeEnum.MORNING_GO, TripTimeEnum.EVENING_BACK)),
    // 校车送孩子回家
    TWO_ROUND_TRIP("4", "往返2次", Arrays.asList(TripTimeEnum.MORNING_GO, TripTimeEnum.MIDDAY_BACK, TripTimeEnum.MIDDAY_GO, TripTimeEnum.EVENING_BACK)),
    ;

    RouteTypeEnum(String code, String desc, List<TripTimeEnum> tripTime) {
        this.code = code;
        this.desc = desc;
        this.tripTime = tripTime;
    }

    private String code;
    private String desc;
    private List<TripTimeEnum> tripTime;

    @JsonValue
    @Override
    public String getValue() {
        return this.code;
    }

    public List<TripTimeEnum> getTripTime() {
        return this.tripTime;
    }

    @JsonCreator
    public static RouteTypeEnum of(String value) throws BusApiException {
        if (null == value) {
            return null;
        }
        for (RouteTypeEnum item : RouteTypeEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new BusApiException("未知类型: " + value);
    }

    public Map<String, Object> toMap(){
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("desc", desc)
                .put("tripTime", tripTime)
                .build();
    }

}
