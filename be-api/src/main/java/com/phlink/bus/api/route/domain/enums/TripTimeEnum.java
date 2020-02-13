package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum TripTimeEnum implements IEnum<String> {
    // 早上送
    MORNING_GO("1", "早上送", TripRedirectEnum.GO),
    // 中午回
    MIDDAY_BACK("2", "中午回", TripRedirectEnum.BACK),
    // 中午接
    MIDDAY_GO("3", "中午送", TripRedirectEnum.GO),
    // 晚上接
    EVENING_BACK("4", "晚上回", TripRedirectEnum.BACK),
    ;

    TripTimeEnum(String code, String desc, TripRedirectEnum directionId) {
        this.code = code;
        this.desc = desc;
        this.directionId = directionId;
    }

    private String code;
    private String desc;
    private TripRedirectEnum directionId;

    @JsonValue
    @Override
    public String getValue() {
        return this.code;
    }

    public TripRedirectEnum getDirectionId() {
        return this.directionId;
    }

    @JsonCreator
    public static TripTimeEnum of(String value) {
        if (null == value) {
            return null;
        }
        for (TripTimeEnum item : TripTimeEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("TripTimeEnum: unknown value: " + value);
    }

    public Map<String, Object> toMap(){
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("desc", desc)
                .build();
    }
}
