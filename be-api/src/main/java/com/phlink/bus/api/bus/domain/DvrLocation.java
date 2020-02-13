package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
* 手环设备定位数据
*
* @author wen
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_dvr_location")
public class DvrLocation {


    @JsonIgnore
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 校车ID
     */
    @ApiModelProperty(value = "校车ID")
    private Long busId;

    /**
     * 校车ID
     */
    @ApiModelProperty(value = "校车ID")
    private Long tid;

    /**
     * dvr设备号
     */
    @ApiModelProperty(value = "dvr设备号")
    private String dvrno;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Long lat;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Long lon;

    /**
     * 方向
     */
    @ApiModelProperty(value = "方向")
    private Integer direction;

    /**
     * 高度(m)
     */
    @ApiModelProperty(value = "高度(m)")
    private BigDecimal altitude;

    /**
     * 里程(1/10km)
     */
    @ApiModelProperty(value = "里程(1/10km)")
    private BigDecimal mileage;

    /**
     * 油量(1/10L)
     */
    @ApiModelProperty(value = "油量(1/10L)")
    private BigDecimal oil;

    /**
     * 速度
     */
    @ApiModelProperty(value = "速度")
    private BigDecimal speed;

    /**
     * 行驶记录仪速度(1/10 km/h)
     */
    @ApiModelProperty(value = "行驶记录仪速度(1/10 km/h)")
    private BigDecimal odbspeed;

    /**
     * 卫星数
     */
    @ApiModelProperty(value = "卫星数")
    private Integer satellites;

    /**
     * 定位时间
     */
    @ApiModelProperty(value = "定位时间")
    private Long gpstime;

    /**
     * 状态位
     */
    @ApiModelProperty(value = "状态位")
    private Integer status1;

    /**
     * 扩展信号位
     */
    @ApiModelProperty(value = "扩展信号位")
    private Long status2;

    /**
     * IO状态位
     */
    @ApiModelProperty(value = "IO状态位")
    private Integer status3;

    /**
     * 报警位
     */
    @ApiModelProperty(value = "报警位")
    private Long status4;

    /**
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态")
    private Integer online;

    /**
     * 经度(gcj02,google,高德等地图)
     */
    @ApiModelProperty(value = "经度(gcj02,google,高德等地图)")
    private BigDecimal glon;

    /**
     * 纬度(gcj02,google,高德等地图)

     */
    @ApiModelProperty(value = "纬度(gcj02,google,高德等地图) ")
    private BigDecimal glat;

    /**
     * 经度(bd09ii,百度地图)
     */
    @ApiModelProperty(value = "经度(bd09ii,百度地图)")
    private BigDecimal blon;

    /**
     * 纬度(bd09ii,百度地图)
     */
    @ApiModelProperty(value = "纬度(bd09ii,百度地图)")
    private BigDecimal blat;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "校车编号")
    private String busCode;


}
