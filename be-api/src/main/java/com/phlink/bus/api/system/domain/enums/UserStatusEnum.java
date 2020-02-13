package com.phlink.bus.api.system.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum UserStatusEnum implements IEnum<String> {
    // 冻结
    INVALIDATE("0", "冻结"),
    // 正常
    EFFECTIVE("1", "正常")
    ;

    UserStatusEnum(String code, String desc) {
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