package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;
/**
* 
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_teacher_checkout_end")
@Excel("随车老师收车检查")
public class TeacherCheckoutEnd extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 卫生是否完成
     */
    @ApiModelProperty(value = "卫生是否完成")
    private Boolean cleaningInfo;

    /**
     * 是否有物品遗漏
     */
    @ApiModelProperty(value = "是否有物品遗漏")
    private Boolean missingInfo;

    /**
     * 卫生情况说明
     */
    @ApiModelProperty(value = "卫生情况说明")
    private String cleaningRemark;

    /**
     * 遗留物品说明
     */
    @ApiModelProperty(value = "遗留物品说明")
    private String missingRemark;

    /**
     * 应乘车人数
     */
    @ApiModelProperty(value = "应乘车人数")
    private Integer enrollNum;

    /**
     * 实际乘车人数
     */
    @ApiModelProperty(value = "实际乘车人数")
    private Integer rideNum;

    /**
     * 请假人数
     */
    @ApiModelProperty(value = "请假人数")
    private Integer leaveNum;

    /**
     * 特殊事项描述
     */
    @ApiModelProperty(value = "特殊事项描述")
    private String description;

    private String[] imagePath;

    private String[] videoPath;

    /**
     * 检查人
     */
    @ApiModelProperty(value = "检查人")
    private Long userId;

    /**
     * 检查人姓名
     */
    @ApiModelProperty(value = "检查人姓名")
    private String realname;

    /**
     * 检查人手机号
     */
    @ApiModelProperty(value = "检查人手机号")
    private String mobile;

    /**
     * 检查时间
     */
    @ApiModelProperty(value = "检查时间")
    private LocalDateTime checkTime;

    private LocalDate checkDate;

    /**
     * 绑定的车辆ID
     */
    @ApiModelProperty(value = "绑定的车辆ID")
    private Long busId;

    /**
     * 车架号/车架号
     */
    @ApiModelProperty(value = "车架号/车架号")
    private String busCode;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String numberPlate;

    /**
     * dvr编号
     */
    @ApiModelProperty(value = "dvr编号")
    private String dvrCode;


}
