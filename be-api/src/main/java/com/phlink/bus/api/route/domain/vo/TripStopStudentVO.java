package com.phlink.bus.api.route.domain.vo;

import lombok.Data;

@Data
public class TripStopStudentVO {
	private String totalCount;
	private String attendCount;
	private String leaveCount;
	private String stopName;
	private Long stopId;
	private String seq;
}
