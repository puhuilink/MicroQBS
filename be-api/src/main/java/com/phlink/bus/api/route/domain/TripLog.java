package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_trip_log")
public class TripLog extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 行程ID
     */
    @ApiModelProperty(value = "行程ID")
    private Long tripId;

    /**
     * 行程标记
     */
    @ApiModelProperty(value = "行程标记")
    private String tripTime;

    /**
     * 路线ID
     */
    @ApiModelProperty(value = "路线ID")
    private Long routeId;

    /**
     * 行程日期
     */
    @ApiModelProperty(value = "行程日期")
    private LocalDate time;

    /**
     * 行程开始时间
     */
    @ApiModelProperty(value = "行程开始时间")
    private LocalTime tripBeginTime;

    /**
     * 行程结束时间
     */
    @ApiModelProperty(value = "行程结束时间")
    private LocalTime tripEndTime;

    /**
     * 1:行驶中，2：已完成
     *
     * 应该对应logRunningState
     */
    @ApiModelProperty(value = "-1:系统自动启动 -2:系统自动关闭 1:行驶中，2：已完成")
    private String state;

    @ApiModelProperty(value = "随车老师ID")
    private Long busTeacherId;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;
    @ApiModelProperty(value = "车辆ID")
    private Long busId;
    @ApiModelProperty(value = "车架号")
    private String busCode;
    @ApiModelProperty(value = "车辆型号")
    private String busModel;
    @ApiModelProperty(value = "车牌号")
    private String busNumberPlate;
    @ApiModelProperty(value = "dvr编号")
    private String dvrno;
    @ApiModelProperty(value = "请假人数")
    private Integer leaveNum;
    @ApiModelProperty(value = "上车人数")
    private Integer upNum;
    @ApiModelProperty(value = "下车人数")
    private Integer downNum;
    @ApiModelProperty(value = "实际绑定站点人数")
    private Integer studentNum;

}
