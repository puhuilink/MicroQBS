package com.phlink.bus.api.serviceorg.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentBaseInfoVO {

    @NotNull(message = "{required}")
    private Long studentId;
    @ApiModelProperty(value = "姓名")
    private String studentName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "出生年月日")
    private LocalDate birthday;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("学校")
    private String schoolName;

}
