package com.phlink.bus.api.system.domain.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "LoginMobilePasswordVO", description = "手机号密码登录请求")
@Data
@Validated
public class LoginMobilePasswordVO {

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "{required}")
    private String mobile;

    @Size(min = 6, max = 24, message = "{range}")
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "{required}")
    private String password;
}
