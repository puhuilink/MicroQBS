package com.phlink.bus.api.fence.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum RelationTypeEnum implements IEnum<String> {
    SCHOOL("1","学校"),
    ROUTE("2","路线"),
    STOP("3","站点");

    RelationTypeEnum(String code, String desc) {
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
