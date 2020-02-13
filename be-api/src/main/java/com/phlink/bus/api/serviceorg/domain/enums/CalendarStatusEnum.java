package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum CalendarStatusEnum implements IEnum<String> {
    // 可用
    AVAILABLE("1", "可用"),
    // 不可用
    UNAVAILABLE("0", "不可用")
    ;

    CalendarStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    @Override
    public String getValue() {
        return this.code;
    }
}
