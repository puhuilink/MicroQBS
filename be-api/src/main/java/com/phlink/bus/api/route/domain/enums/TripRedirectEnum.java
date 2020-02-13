package com.phlink.bus.api.route.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

public enum TripRedirectEnum implements IEnum<Integer> {
    // 校车接孩子
    GO(1, "校车接孩子"),
    // 校车送孩子回家
    BACK(-1, "校车送孩子回家"),
    ;

    TripRedirectEnum(Integer code, String desc) {
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
