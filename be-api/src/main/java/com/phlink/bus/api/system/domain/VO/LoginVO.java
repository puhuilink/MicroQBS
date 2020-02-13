package com.phlink.bus.api.system.domain.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "loginVO", description = "账号密码登录请求")
@Data
@Validated
public class LoginVO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{required}")
    private String username;

    @Size(min = 6, max = 24, message = "{range}")
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "{required}")
    private String password;
}
