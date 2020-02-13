package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateRouteVO extends SaveRouteVO{
    @ApiModelProperty(value = "路线ID")
    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long routeId;

    @ApiModelProperty(value = "删除的站点ID列表")
    private List<Long> deleteStopIds;
}
