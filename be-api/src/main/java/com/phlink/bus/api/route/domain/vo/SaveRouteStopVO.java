package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.route.controller.validation.OnPointStop;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class SaveRouteStopVO {

    @ApiModelProperty(value = "站点名称")
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class, OnUpdate.class})
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private String stopName;

    @ApiModelProperty(value = "经度")
    @DecimalMax("1000.000000")
    @DecimalMin("0.000001")
    @Digits(integer=3,fraction=6)
    @NotNull(message = "{required}", groups = {OnPointStop.class})
    @ExcelField(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    @DecimalMax("1000.000000")
    @DecimalMin("0.000001")
    @Digits(integer=3,fraction=6)
    @NotNull(message = "{required}", groups = {OnPointStop.class})
    @ExcelField(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "站点在路线中的顺序")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Integer stopSequence;

    @NotNull(message = "{required}", groups = {OnAdd.class})
    private TripTimeEnum tripTime;

    @ApiModelProperty(value = "到达时间")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime arrivalTime;

    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long stopTimeId;

    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long tripId;
}
