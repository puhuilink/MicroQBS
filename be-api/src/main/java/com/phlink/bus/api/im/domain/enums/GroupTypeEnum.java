package com.phlink.bus.api.im.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum GroupTypeEnum implements IEnum<Integer> {
    // 自建群
    CUSTOMIZE(0, "自建群"),
    // 部门群
    DEPT(1, "部门群"),
    // 公司官方群
    COMPANY(2, "公司官方群"),
    ;

    GroupTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    @Override
    public Integer getValue() {
        return this.code;
    }
}
