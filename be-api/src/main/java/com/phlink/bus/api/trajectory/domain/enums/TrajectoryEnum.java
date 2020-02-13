package com.phlink.bus.api.trajectory.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum TrajectoryEnum implements IEnum<String> {
    CREATE("1","已生成"),
    UNCREATE("0","未生成");

    TrajectoryEnum(String code, String desc) {
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
