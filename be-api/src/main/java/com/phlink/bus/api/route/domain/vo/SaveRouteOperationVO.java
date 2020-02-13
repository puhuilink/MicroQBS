package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SaveRouteOperationVO {

    @ApiModelProperty(value = "ID")
    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long id;
    /**
     * 绑定车辆ID
     */
    @ApiModelProperty(value = "绑定校车ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindBusId;

    /**
     * 绑定随车老师ID
     */
    @ApiModelProperty(value = "绑定随车老师ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindBusTeacherId;

    /**
     * 绑定司机ID
     */
    @ApiModelProperty(value = "绑定司机ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindDriverId;

    /**
     * 路线ID
     */
    @ApiModelProperty(value = "路线ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long routeId;

    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private List<BindStudentVO> bindStudents;
//    private List<BindStudentVO> unbindStudents;

}
