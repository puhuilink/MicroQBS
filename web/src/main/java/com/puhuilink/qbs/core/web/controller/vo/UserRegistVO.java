package com.puhuilink.qbs.core.web.controller.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import com.puhuilink.qbs.core.common.validate.tag.OnAdd;
import lombok.Data;

@Data
public class UserRegistVO {
    @NotBlank(message = "{required}", groups = { OnAdd.class })
    private String username;
    private String email;
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = { OnAdd.class })
    private String realname;
    private String mobile;
    @NotBlank(message = "{required}", groups = { OnAdd.class })
    @Size(min = 6, max = 24, message = "{range}")
    private String password;
}
