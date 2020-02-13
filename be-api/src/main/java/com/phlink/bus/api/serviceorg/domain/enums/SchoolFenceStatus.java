package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum SchoolFenceStatus implements IEnum<String> {
    // 未配置
    UNCONFIGURED("0", "未配置"),
    // 已配置
    CONFIGURED("1", "已配置")
    ;

    SchoolFenceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    @Override
    public String getValue() {
        return this.code;
    }

    @JsonCreator
    public static SchoolFenceStatus of(String value) {
        if (null == value) {
            return null;
        }
        for (SchoolFenceStatus item : SchoolFenceStatus.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("SchoolFenceStatus: unknown value: " + value);
    }

    public Map<String, Object> toMap(){
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("desc", desc)
                .build();
    }
}
