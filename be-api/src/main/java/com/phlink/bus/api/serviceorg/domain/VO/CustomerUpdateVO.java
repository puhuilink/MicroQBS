package com.phlink.bus.api.serviceorg.domain.VO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerUpdateVO {

    @NotNull(message = "{required}")
    private Long userId;
    @NotNull(message = "{required}")
    private String realname;
    @NotNull(message = "{required}")
    private String mobile;

    private String idcard;

}
