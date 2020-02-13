package com.phlink.bus.api.alarm.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum AlarmSubTypeEnum implements IEnum<String> {
    DIVERGE("1", "偏离路线"),
    SPEEDING("2", "车辆超速"),
    DELAY("3", "站点迟到"),
    MONITORED("4", "监控设备"),
    LEAVE_SCHOOL("5", "异常出校");

    AlarmSubTypeEnum(String code, String desc) {
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