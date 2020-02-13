package com.phlink.bus.api.route.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("站点学生")
public class StopStudentVo {
	private Long id;
	private String name;
	private String mobile;
	private Long stopId;
	private Long tripId;
	// ARRIVE,  LEAVE, WAIT
	@ApiModelProperty(value = "状态值：ARRIVE,  LEAVE, WAIT")
	private String stat;
}
