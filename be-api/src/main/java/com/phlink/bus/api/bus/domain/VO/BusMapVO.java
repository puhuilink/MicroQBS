package com.phlink.bus.api.bus.domain.VO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class BusMapVO {
    /**
     * 车辆编码
     */
    @NotBlank(message = "{required}")
    private String busCode;
    /**
     * 车辆牌照
     */
    @NotBlank(message = "{required}")
    private String numberPlate;
}
