package com.phlink.bus.api.trajectory.domain;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* 轨迹
*
* @author zhouyi
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_trajectory")
public class Trajectory extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 轨迹id
     */
    @ApiModelProperty(value = "轨迹id")
    private Long tid;

    /**
     * 轨迹名称
     */
    @ApiModelProperty(value = "轨迹名称")
    private String name;

    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    /**
     * 时长
     */
    @ApiModelProperty(value = "时长(秒)")
    private Long duration;

    /**
     * 关联行程记录ID
     */
    @ApiModelProperty(value = "关联行程记录ID")
    private Long tripLogId;

    /**
     * 关联行程ID
     */
    @ApiModelProperty(value = "关联行程ID")
    private Long tripId;

    /**
     * 关联路线ID
     */
    @ApiModelProperty(value = "关联路线ID")
    private Long routeId;

    /**
     * 轨迹坐标集
     */
    @ApiModelProperty(value = "轨迹坐标集")
    private transient JSONArray points;

    /**
     * 轨迹长度（米）
     */
    @ApiModelProperty(value = "轨迹长度（米）")
    private Long distance;

    /**
     * 轨迹点数量
     */
    @ApiModelProperty(value = "轨迹点数量")
    private Long counts;

    @ApiModelProperty("车牌号")
    private String numberPlate;

    @ApiModelProperty("发动机型号")
    private String engineModel;

    @ApiModelProperty("车架号")
    private String busCode;

    @ApiModelProperty("车辆ID")
    private Long busId;

    @ApiModelProperty("轨迹id(地图)")
    private Long trid;

    private transient double[][] simplePoints;
}
