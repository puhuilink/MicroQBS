package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceStatusEnum implements IEnum<String> {
    // 已失效
    INEFFECTIVE("0", "已失效"),
    // 已生效
    EFFECTIVE("1", "已生效")
    ;

    ServiceStatusEnum(String code, String desc) {
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
}
