package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.annotation.DistributedLockParam;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.converter.CustomizeFieldReadToLocalDateConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 车辆信息
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_bus")
@Excel("车辆信息表")
public class Bus extends ApiBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 车牌号
     */
    @DistributedLockParam(name = "numberPlate")
//    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "车牌号")
    @ApiModelProperty("车牌号")
    private String numberPlate;

    /**
     * 车架号
     */
    @DistributedLockParam(name = "busCode")
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "车架号")
    @ApiModelProperty("车架号")
    private String busCode;

    /**
     * 车辆型号
     */
    @ExcelField(value = "车辆型号")
    @ApiModelProperty("车辆型号")
    private String model;

    /**
     * 车辆厂商
     */
    @ExcelField(value = "车辆厂商")
    @ApiModelProperty("车辆厂商")
    private String brand;

    /**
     * 底盘
     */
    @ExcelField(value = "底盘编号")
    @ApiModelProperty("底盘编号")
    private String chassis;

    /**
     * 发动机型号
     */
    @ExcelField(value = "发动机型号")
    @ApiModelProperty("发动机型号")
    private String engineModel;

    /**
     * 额定功率
     */
    @ExcelField(value = "额定功率")
    @ApiModelProperty("额定功率")
    private String power;

    /**
     * 排放标准
     */
    @ExcelField(value = "排放标准")
    @ApiModelProperty("排放标准")
    private String emission;

    /**
     * 长(mm)
     */
    @ExcelField(value = "长(mm)")
    @ApiModelProperty("长(mm)")
    private String length;

    /**
     * 宽(mm)
     */
    @ExcelField(value = "宽(mm)")
    @ApiModelProperty("宽(mm)")
    private String width;

    /**
     * 高(mm)
     */
    @ExcelField(value = "高(mm)")
    @ApiModelProperty("高(mm)")
    private String height;

    /**
     * 座位数量
     */
    @ExcelField(value = "座位数量")
    @ApiModelProperty("座位数量")
    private String seat;

    /**
     * 燃料箱容量
     */
    @ExcelField(value = "燃料箱容量")
    @ApiModelProperty("燃料箱容量")
    private String fuelTank;

    /**
     * 所属部门
     */
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty("所属部门")
    private Long deptId;

    /**
     * 所属部门名称
     */
    @ExcelField(value = "所属部门")
    @ApiModelProperty("所属部门名称")
    private transient String deptName;

    /**
     * 终端标识（百度）
     */
    @ApiModelProperty(value = "终端标识（百度）", hidden = true)
    private String entityName;

    /**
     * 终端标识(高德)
     */
    @ApiModelProperty(value = "终端标识(高德)", hidden = true)
    private long tid;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("操作人")
    private Long modifyBy;

    @ExcelField(value = "车辆状态")
    @ApiModelProperty("车辆状态")
    private transient String busState;

    /**
     * 绑定的DVR设备编号
     */
    @ExcelField(value = "绑定DVR设备")
//    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty("绑定的DVR设备")
    private transient String dvrCode;

    /**
     * DVR的摄像头通道数量
     */
//    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty("DVR的摄像头通道数量")
    private transient Integer channelNumber;

    @ExcelField(value = "车辆注册时间", readConverter = CustomizeFieldReadToLocalDateConverter.class)
    @ApiModelProperty("车辆注册时间")
    private LocalDate registerTime;
    @ExcelField(value = "车辆验车时间", readConverter = CustomizeFieldReadToLocalDateConverter.class)
    @ApiModelProperty("车辆验车时间")
    private LocalDate checkTime;
    @ExcelField(value = "车辆保险时间", readConverter = CustomizeFieldReadToLocalDateConverter.class)
    @ApiModelProperty("车辆保险时间")
    private LocalDate insureTime;
    @ExcelField(value = "车辆保养时间", readConverter = CustomizeFieldReadToLocalDateConverter.class)
    @ApiModelProperty("车辆保养时间")
    private LocalDate maintainTime;

}
