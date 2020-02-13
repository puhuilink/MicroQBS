package com.phlink.bus.api.alarm.domain.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;

public enum AlarmTypeEnum implements IEnum<String> {
    ROUTE("1", "路线告警"),
    STOP("2", "站点告警"),
    SCHOOL("3", "学校告警"),
    BUS("4", "车辆告警");

    AlarmTypeEnum(String code, String desc) {
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
