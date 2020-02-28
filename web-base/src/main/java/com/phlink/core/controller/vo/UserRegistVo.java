package com.phlink.core.controller.vo;

import com.phlink.core.common.validation.tag.OnAdd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRegistVo {
    @NotBlank(message = "{required}", groups = {OnAdd.class})
    private String username;
    private String email;
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class})
    private String realname;
    private String mobile;
    @NotBlank(message = "{required}", groups = {OnAdd.class})
    @Size(min = 6, max = 24, message = "{range}")
    private String password;
}
