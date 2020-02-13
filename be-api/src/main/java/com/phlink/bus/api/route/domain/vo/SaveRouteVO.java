package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.List;

@Data
public class SaveRouteVO {
    @ApiModelProperty(value = "路线名称")
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class, OnUpdate.class})
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private String routeName;

    @ApiModelProperty(value = "路线描述")
    private String routeDesc;

    @ApiModelProperty(value = "路线类型")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private RouteTypeEnum routeType;

    @ApiModelProperty(value = "所属学校ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long schoolId;

    @ApiModelProperty(value = "轨迹ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long trajectoryId;

    @ApiModelProperty(value = "出站时间(始发时间)")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime outboundTime;

    private List<SaveRouteStopVO> stops;
}
