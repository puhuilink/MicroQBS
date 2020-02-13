package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 站点考勤
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("学生站点打卡")
@TableName("t_stop_attendance")
public class StopAttendance extends ApiBaseEntity {

    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    @ApiModelProperty(value = "打卡日期")
    private LocalDate time;

    @ApiModelProperty(value = "打卡类型")
    private String type;

    @ApiModelProperty(value = "站点ID")
    private Long stopId;

    @ApiModelProperty(value = "站点名称")
    private String stopName;

    @ApiModelProperty(value = "路线ID")
    private Long routeId;

    @ApiModelProperty(value = "路线名称")
    private String routeName;

    @ApiModelProperty(value = "行程ID")
    private Long tripId;

    @ApiModelProperty(value = "手动打卡原因")
    private Long reason;

    @ApiModelProperty(value = "随车老师ID")
    private Long busTeacherId;
    @ApiModelProperty(value = "随车老师姓名")
    private String busTeacherName;
    @ApiModelProperty(value = "司机ID")
    private Long driverId;
    @ApiModelProperty(value = "司机姓名")
    private String driverName;
    @ApiModelProperty(value = "车辆ID")
    private Long busId;
    @ApiModelProperty(value = "车辆编号")
    private String busCode;
    @ApiModelProperty(value = "车辆车牌号")
    private String numberPlate;

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


}
