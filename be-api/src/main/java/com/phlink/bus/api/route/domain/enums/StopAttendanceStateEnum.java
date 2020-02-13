package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum StopAttendanceStateEnum implements IEnum<String> {

    LEAVE("-1", "已请假"),
    WAIT("0", "未上车"),
    UP("1", "已上车"),
    DOWN("2", "已下车"),
    ;

    StopAttendanceStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    @JsonValue
    @Override
    public String getValue() {
        return this.code;
    }

    @JsonCreator
    public static StopAttendanceStateEnum of(String value) {
        if (null == value) {
            return null;
        }
        for (StopAttendanceStateEnum item : StopAttendanceStateEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("RouteTypeEnum: unknown value: " + value);
    }

    public Map<String, Object> toMap(){
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("desc", desc)
                .build();
    }

}
