package com.phlink.bus.api.device.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum DeviceStatusEnum implements IEnum<String> {
    NORMAL("0", "正常"),
    REPAIR("1", "维修"),
    DAMAGE("2", "损坏");

    DeviceStatusEnum(String code, String desc) {
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
