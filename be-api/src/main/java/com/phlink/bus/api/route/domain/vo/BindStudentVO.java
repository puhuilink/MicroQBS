package com.phlink.bus.api.route.domain.vo;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BindStudentVO {

//    @NotEmpty(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private List<Long> studentIds;

    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long stopId;

    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long routeOperationId;
}
