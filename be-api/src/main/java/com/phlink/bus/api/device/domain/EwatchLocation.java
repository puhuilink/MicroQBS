package com.phlink.bus.api.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.rpc.core.device.EWatchInfo;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("t_ewatch_location")
public class EwatchLocation {

    @JsonIgnore
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    /**
     * 位置产生时间
     */
    @ApiModelProperty(value = "位置产生时间")
    private Long timestamp;

    /**
     * 电池电量
     */
    @ApiModelProperty(value = "电池电量")
    private Integer batteryLevel;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal latitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal longitude;

    /**
     * 速度
     */
    @ApiModelProperty(value = "速度")
    private Integer speed;

    /**
     * 方向
     */
    @ApiModelProperty(value = "方向")
    private Integer direction;

    /**
     * 创建时间
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    private transient Long studentId;
    private transient String studentName;
    private transient String mainGuardianName;
    private transient String mainGuardianMobile;
    private transient String schoolName;

    public EwatchLocation(EWatchInfo eWatchInfo) {
        if(eWatchInfo != null) {
            this.deviceId = eWatchInfo.getDeviceId();
            this.timestamp = eWatchInfo.getTimestamp();
            this.batteryLevel = eWatchInfo.getBatteryLevel();
            this.latitude = BigDecimal.valueOf(eWatchInfo.getLatitude());
            this.longitude = BigDecimal.valueOf(eWatchInfo.getLongitude());
            this.speed = eWatchInfo.getSpeed();
            this.direction = eWatchInfo.getDirection();
        }
    }
}
