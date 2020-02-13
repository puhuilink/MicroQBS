package com.phlink.bus.api.serviceorg.domain.VO;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class GuarDianLeaveApplyStatusVO {
	@NotBlank(message = "{required}")
	private Long guardianId;

	@NotBlank(message = "{required}")
	private Boolean leaveApplyStatus;

}
