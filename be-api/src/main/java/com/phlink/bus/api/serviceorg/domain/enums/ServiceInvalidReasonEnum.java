package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum ServiceInvalidReasonEnum implements IEnum<String> {

    EXPIRE("1", "已到期"),

    TRANSFER("2", "已转校"),

    OTHER("9", "其他"),
    ;

    ServiceInvalidReasonEnum(String code, String desc) {
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
