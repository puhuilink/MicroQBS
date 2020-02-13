package com.phlink.bus.api.attendance.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;
/**
* 
*
* @author ZHOUY
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_bus_attendance")
@Excel("司乘签到信息表")
public class BusAttendance extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

    /**
     * 打卡人员
     */
    @ApiModelProperty(value = "打卡人员")
    private Long userId;

    /**
     * 车牌号
     */
    @ExcelField("车牌号")
    @ApiModelProperty(value = "车牌号")
    private String numberPlate;

    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号")
    private Long busId;

    /**
     * 路线id
     */
    @ApiModelProperty(value = "路线id")
    private Long routeId;

    /**
     * 1:发车打卡，2：收车打卡
     */
    @ExcelField("签到类别")
    @ApiModelProperty(value = "1:发车打卡，2：收车打卡")
    private String type;

    /**
     * 打卡日期
     */
    @ExcelField("签到日期")
    @ApiModelProperty(value = "打卡日期")
    private LocalDate time;
    
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String reason;
    
    /**
     * 打卡位置
     */
    @ExcelField("签到位置")
    @ApiModelProperty(value = "打卡位置")
    private String position;


}
