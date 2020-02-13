package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.bus.domain.enums.AttachmentTypeEnum;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_teacher_checkout")
@Excel("随车老师晨检信息")
public class TeacherCheckout extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ExcelField(value = "创建时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

    /**
     * 创建日期
     */
    @ExcelField(value = "检查日期")
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
    @ExcelField(value = "检查人姓名")
    @ApiModelProperty(value = "检查人姓名")
    private String userName;

    /**
     * 检查人手机号
     */
    @ExcelField(value = "检查人手机号")
    @ApiModelProperty(value = "检查人手机号")
    private String mobile;

    /**
     * 汽车车牌号
     */
    @ExcelField(value = "汽车车牌号")
    @ApiModelProperty(value = "汽车车牌号")
    private String numberPlate;

    /**
     * 监控设施
     */
    @ExcelField(value = "监控设施", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "监控设施")
    private Boolean monitor;

    /**
     * 安全六项
     */
    @ExcelField(value = "安全六项", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "安全六项")
    private Boolean security;

    /**
     * 钥匙包
     */
    @ExcelField(value = "钥匙包", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "钥匙包")
    private Boolean key;

    /**
     * 消毒
     */
    @ExcelField(value = "消毒", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "消毒")
    private Boolean disinfect;

    /**
     * 卫生
     */
    @ExcelField(value = "卫生", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "卫生")
    private Boolean hygienism;

    /**
     * 岗位备品
     */
    @ExcelField(value = "岗位备品", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "岗位备品")
    private Boolean post;

    /**
     * 医药用品
     */
    @ExcelField(value = "医药用品", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "医药用品")
    private Boolean medicinal;

    /**
     * 车灯
     */
    @ExcelField(value = "车灯", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "车灯")
    private Boolean carlight;

    /**
     * 手机工作
     */
    @ExcelField(value = "手机工作", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "手机工作")
    private Boolean cellphone;

    /**
     * 司机状态
     */
    @ExcelField(value = "司机状态", writeConverterExp = "true=正常,false=异常")
    @ApiModelProperty(value = "司机状态")
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
