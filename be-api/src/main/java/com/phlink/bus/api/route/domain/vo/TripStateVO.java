package com.phlink.bus.api.route.domain.vo;

import lombok.Data;

@Data
public class TripStateVO {
	private Long tripId;
	private Long routeId;
	private String tripTime;
	private String state;
	private Long logId;
}
