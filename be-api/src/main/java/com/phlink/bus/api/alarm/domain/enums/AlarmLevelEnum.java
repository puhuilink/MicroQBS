package com.phlink.bus.api.alarm.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum AlarmLevelEnum implements IEnum<String> {
    SLIGHT("0", "轻微"),
    LOW("1", "低"),
    MIDDLE("2", "中"),
    DELAY("3", "高");

    AlarmLevelEnum(String code, String desc) {
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