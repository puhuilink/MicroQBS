package com.phlink.bus.api.serviceorg.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClassesViewVO {

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "入学年份",example = "2019")
    private Integer enrollYear;

    @ApiModelProperty(value = "年级升级版，随系统年份增长，和school_system一起用于判断是否毕业",example = "1")
    private Integer gradePro;
}
