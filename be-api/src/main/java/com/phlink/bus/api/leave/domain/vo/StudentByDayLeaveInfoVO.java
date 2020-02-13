package com.phlink.bus.api.leave.domain.vo;

import lombok.Data;

@Data
public class StudentByDayLeaveInfoVO {
    private Integer busTime;
    private Long studentId;
    private String signImg;
}
