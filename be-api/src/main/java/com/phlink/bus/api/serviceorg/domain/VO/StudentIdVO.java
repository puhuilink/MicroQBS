package com.phlink.bus.api.serviceorg.domain.VO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudentIdVO {

    @NotNull(message = "{required}")
    private Long studentId;

}
