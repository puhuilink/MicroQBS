package com.phlink.bus.api.alarm.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum MessageTypeEnum implements IEnum<String> {
    BUS("1", "车辆"),
    DEVICE("2", "手环");

    MessageTypeEnum(String code, String desc) {
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
