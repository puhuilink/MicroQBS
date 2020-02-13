package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
* 一条route下最多可以有4条trip
*
* @author wen
*/
@ApiModel(value = "行程对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_trip")
public class Trip extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 路线ID
     */
    @ApiModelProperty(value = "路线ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long routeId;

    @ApiModelProperty(value = "学校ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long schoolId;

    @ApiModelProperty(value = "行程时间类型")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private TripTimeEnum tripTime;

    /**
     * 同一路线的不同方向
     */
    @ApiModelProperty(value = "行程方向，1：去程； -1：返程")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private TripRedirectEnum directionId;

}
