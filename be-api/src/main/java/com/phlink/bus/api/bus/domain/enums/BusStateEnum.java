package com.phlink.bus.api.bus.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BusStateEnum implements IEnum<Integer> {
    STORAGE(1, "已入库"),
    BIND(2, "已绑定")
    ;

    BusStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    @JsonValue
    @Override
    public Integer getValue() {
        return null;
    }
}
