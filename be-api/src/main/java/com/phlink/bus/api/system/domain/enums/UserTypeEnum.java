package com.phlink.bus.api.system.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum UserTypeEnum implements IEnum<String> {
    // 后台管理员
    ADMIN("0", "后台管理员"),
    // 会员
    MEMBER("1", "前台会员")
    ;

    UserTypeEnum(String code, String desc) {
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
