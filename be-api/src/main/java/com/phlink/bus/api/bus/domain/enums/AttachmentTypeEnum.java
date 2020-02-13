package com.phlink.bus.api.bus.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AttachmentTypeEnum implements IEnum<String> {
    // 视频
    VIDEO("VIDEO"),
    // 图片
    IMAGE("IMAGE")
    ;

    AttachmentTypeEnum(String code) {
        this.code = code;
    }

    private String code;

    @JsonValue
    @Override
    public String getValue() {
        return this.code;
    }
}
