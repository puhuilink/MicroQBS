package com.phlink.bus.api.map.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum MapTypeEnum implements IEnum<String> {
    //百度
    BAIDU("0", "baidu"),
    //高德
    AMAP("1", "amap");

    MapTypeEnum(String code, String desc) {
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
