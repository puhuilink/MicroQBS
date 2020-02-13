package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
* 站点时刻，对应一条trip记录
*
* @author wen
*/
@ApiModel(value = "站点时刻对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_stop_time")
public class StopTime extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 站点ID
     */
    @ApiModelProperty(value = "站点ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long stopId;

    @ApiModelProperty(value = "行程ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long tripId;

    /**
     * 到达时间
     */
    @ApiModelProperty(value = "到达时间")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime arrivalTime;

    /**
     * 出发时间
     */
    @ApiModelProperty(value = "出发时间", hidden = true)
    private LocalTime departureTime;

    /**
     * Identifies the order of the stops for a particular trip. The values for stop_sequence must increase throughout the trip but do not need to be consecutive.
        For example, the first stop on the trip could have a stop_sequence of 1, the second stop on the trip could have a stop_sequence of 23, the third stop could have a stop_sequence of 40, and so on.
     */
    @ApiModelProperty(value = "站点时刻顺序, 来自站点顺序字段，根据行程的方向，反方向为负")
    private Integer stopSequence;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

    /**
     * 排序字段
     */
    private transient String sortField;

    /**
     * 排序规则 ascend 升序 descend 降序
     */
    private transient String sortOrder;

    /**
     * 创建时间--开始时间
     */
    private transient Long createTimeFrom;

    /**
     * 创建时间--结束时间
     */
    private transient Long createTimeTo;
    private transient String stopName;
    private transient BigDecimal lon;
    private transient BigDecimal lat;


}
