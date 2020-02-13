package com.phlink.bus.api.fence.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public enum RepeatEnum implements IEnum<String> {
    //工作日(周一到周五)
    @ApiModelProperty("工作日(周一到周五)")
    WORKDAY("Mon,Tues,Wed,Thur,Fri", "工作日(周一到周五)"),
    //全天(周一到周日)
    @ApiModelProperty("全天(周一到周日)")
    EVERYDAY("Mon,Tues,Wed,Thur,Fri,Sat,Sun", "全天(周一到周日)");

    RepeatEnum(String code, String desc) {
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