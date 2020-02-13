package com.phlink.bus.api.alarm.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum ProcessingStatusEnum implements IEnum<String> {
    UNPROCESSED("0", "未处理"),
    PROCESSED("1", "系统处理"),
    MANUAL("2", "已人工处理");

    ProcessingStatusEnum(String code, String desc) {
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