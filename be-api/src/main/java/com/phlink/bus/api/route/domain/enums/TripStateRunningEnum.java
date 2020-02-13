package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TripStateRunningEnum implements IEnum<Integer> {
    WAITE(1, "等待运行"),
    RUNNING(2, "运行中"),
    FINISH(3, "完成"),
    TIMEOUT(4, "超时未完成"),
    ;

    TripStateRunningEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    @JsonValue
    @Override
    public Integer getValue() {
        return this.code;
    }
}
