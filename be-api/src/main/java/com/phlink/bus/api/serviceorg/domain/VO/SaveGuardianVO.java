package com.phlink.bus.api.serviceorg.domain.VO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class SaveGuardianVO {
	@NotNull(message = "{required}")
	private Long studentId;
	@NotBlank(message = "{required}")
	private String username;
	@NotBlank(message = "{required}")
	private String mobile;
	@NotBlank(message = "{required}")
	private String idCard;

}
