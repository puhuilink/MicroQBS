package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TripLogRunningEnum implements IEnum<Integer> {
    WAITE(1, "等待运行"),
    RUNNING(2, "运行中"),
    FINISH(3, "完成"),
    ;

    TripLogRunningEnum(Integer code, String desc) {
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
