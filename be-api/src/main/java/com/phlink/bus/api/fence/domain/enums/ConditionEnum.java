package com.phlink.bus.api.fence.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum ConditionEnum implements IEnum<String> {

    ENTER("enter", "进入"),
    LEAVE("leave", "离开"),
    ENTERORLEAVE("enter;leave", "全部");

    ConditionEnum(String code, String desc) {
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
