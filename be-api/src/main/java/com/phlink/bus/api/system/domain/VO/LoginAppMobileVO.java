package com.phlink.bus.api.system.domain.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "loginMobileVO", description = "手机登录/注册请求，如果该用户没有注册，则自动注册")
@Data
@Validated
public class LoginAppMobileVO {
    @ApiModelProperty(value = "用户手机号", required = true)
    @NotBlank(message = "{required}")
    private String mobile;

    @ApiModelProperty(value = "短信验证码", required = true)
    @NotBlank(message = "{required}")
    private String captcha;

    @ApiModelProperty(value = "极光推送设备ID", required = true)
    @NotBlank(message = "{required}")
    private String registrationID;
}
