package com.phlink.bus.api.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.annotation.DistributedLockParam;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.device.domain.enums.DeviceStatusEnum;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备信息表
 *
 * @author zy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device")
@Excel("手环信息")
public class Device extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备名称
     */
    @Size(max = 100, message = "{noMoreThan}")
//    @ExcelField(value = "设备名称")
    @ApiModelProperty("设备名称")
    private String deviceName;

    /**
     * 设备标识码
     */
    @DistributedLockParam(name = "deviceCode")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "设备标识码")
    @ApiModelProperty("设备标识码")
    private String deviceCode;

    /**
     * 设备类型
     */
    @Size(max = 100, message = "{noMoreThan}")
    @ApiModelProperty("设备类型")
    private String deviceType;

    /**
     * 终端标识(高德)
     */
    private long tid;

    /**
     * 设备状态
     */
    @ExcelField(value = "设备状态",writeConverterExp = "NORMAL=正常,REPAIR=维修中,DAMAGE=损坏")
    @ApiModelProperty("设备状态")
    private DeviceStatusEnum deviceStatus;

    /**
     * 绑定状态
     */
    @ExcelField(value = "绑定状态", writeConverterExp = "false=未绑定,true=已绑定")
    @ApiModelProperty("绑定状态")
    private Boolean bindingStatus;

    /**
     * sim
     */
    @Size(max = 20, message = "{noMoreThan}")
    @ApiModelProperty("SIM卡")
    private String sim;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("操作人")
    private Long modifyBy;

    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("学生id")
    private transient Long studentId;

    @ExcelField(value = "学生姓名")
    @ApiModelProperty("学生姓名")
    private transient String studentName;

    @ExcelField(value = "学校名称")
    @ApiModelProperty("学校名称")
    private transient String schoolName;

    @ExcelField(value = "主责人姓名")
    @ApiModelProperty("主责人姓名")
    private transient String mainGuardianName;

    @ExcelField(value = "主责人电话")
    @ApiModelProperty("主责人电话")
    private transient String mainGuardianMobile;

    @ExcelField(value = "绑定时间")
    @ApiModelProperty("绑定时间")
    private transient LocalDateTime bindingTime;

    @ApiModelProperty("位置信息")
    private transient EwatchLocation lastLocation;

    @ExcelField(value = "电量")
    @ApiModelProperty(name = "电量", hidden = true)
    private transient Integer batteryLevel;

    @ExcelField(value = "经度")
    @ApiModelProperty(value = "经度", hidden = true)
    private transient BigDecimal latitude;

    @ExcelField(value = "纬度")
    @ApiModelProperty(value = "纬度", hidden = true)
    private transient BigDecimal longitude;

    @ExcelField(value = "速度")
    @ApiModelProperty(value = "速度", hidden = true)
    private transient Integer speed;

    @ExcelField(value = "方向")
    @ApiModelProperty(value = "方向")
    private transient Integer direction;

    public void setLastLocation(EwatchLocation lastLocation) {
        this.lastLocation = lastLocation;
        this.batteryLevel = lastLocation.getBatteryLevel();
        this.latitude = lastLocation.getLatitude();
        this.longitude = lastLocation.getLongitude();
        this.speed = lastLocation.getSpeed();
        this.direction = lastLocation.getDirection();
    }
}
