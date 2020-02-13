package com.phlink.bus.api.fence.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;


public enum FenceTypeEnum implements IEnum<String> {
    //圆形
    CIRCLE("1", "circle"),
    //多边形
    POLYGON("2", "polygon"),
    //线型
    POLYLINE("3", "polyline");

    FenceTypeEnum(String code, String desc) {
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

