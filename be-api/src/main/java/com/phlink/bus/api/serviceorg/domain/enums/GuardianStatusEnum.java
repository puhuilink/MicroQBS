package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum GuardianStatusEnum implements IEnum<String> {
    // 可用
    NORMAL("1", "正常"),
    // 不可用
    FREEZE("0", "冻结");

    GuardianStatusEnum(String code, String desc) {
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

