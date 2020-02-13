package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.bus.domain.enums.AttachmentTypeEnum;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

/**
* 
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_driver_checkout")
@Excel("司机晨检信息")
public class DriverCheckout extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ExcelField(value = "创建时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

    /**
     * 创建日期
     */
    @ExcelField("创建日期")
    @ApiModelProperty(value = "创建日期")
    private LocalDate time;

    /**
     * 检查人ID
     */
    @ApiModelProperty(value = "检查人ID")
    private Long userId;

    /**
     * 检查人姓名
     */
    @ExcelField("检查人姓名")
    @ApiModelProperty(value = "检查人姓名")
    private String userName;

    /**
     * 检查人手机号
     */
    @ExcelField("检查人手机号")
    @ApiModelProperty(value = "检查人手机号")
    private String mobile;

    /**
     * 汽车车牌号
     */
    @ExcelField("汽车车牌号")
    @ApiModelProperty(value = "汽车车牌号")
    private String numberPlate;

    /**
     * 制动系统
     */
    @ExcelField(value = "制动系统", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "制动系统")
    private Boolean brakingSystem;

    /**
     * 转向系统
     */
    @ExcelField(value = "转向系统", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "转向系统")
    private Boolean steeringSystem;

    /**
     * 座椅和安全带
     */
    @ExcelField(value = "座椅和安全带", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "座椅和安全带")
    private Boolean seat;

    /**
     * 车身和轮胎
     */
    @ExcelField(value = "车身和轮胎", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "车身和轮胎")
    private Boolean car;

    /**
     * 车门
     */
    @ExcelField(value = "车门", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "车门")
    private Boolean door;

    /**
     * 发动机
     */
    @ExcelField(value = "发动机", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "发动机")
    private Boolean engine;

    /**
     * 仪表
     */
    @ExcelField(value= "仪表", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "仪表")
    private Boolean meter;

    /**
     * 随行员工状态
     */
    @ExcelField(value= "随行员工状态", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "随行员工状态")
    private Boolean userState;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value= "特殊事项描述")
    @ApiModelProperty(value = "特殊事项描述")
    private String description;

    @ApiModelProperty(value = "视频路径")
    private String[] videoPath;

    @ApiModelProperty(value = "图片路径")
    private String[] imagePath;

    @ApiModelProperty(value = "附件类型")
    private AttachmentTypeEnum attachmentType;

    /**
    * 创建时间--开始时间
    */
    private transient String createTimeFrom;
    /**
    * 创建时间--结束时间
    */
    private transient String createTimeTo;
}
