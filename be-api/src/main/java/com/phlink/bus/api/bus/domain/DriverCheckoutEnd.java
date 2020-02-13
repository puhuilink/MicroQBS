package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;
/**
* 司机收车检查
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_driver_checkout_end")
@Excel("司机收车检查")
public class DriverCheckoutEnd extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 路况是否正常
     */
    @ApiModelProperty(value = "路况是否正常")
    private Boolean roadInfo;

    /**
     * 驾驶是否正常
     */
    @ApiModelProperty(value = "驾驶是否正常")
    private Boolean driveInfo;

    /**
     * 加油是否正常
     */
    @ApiModelProperty(value = "加油是否正常")
    private Boolean oilInfo;

    /**
     * 路况说明
     */
    @ApiModelProperty(value = "路况说明")
    private String roadRemark;

    /**
     * 驾驶说明
     */
    @ApiModelProperty(value = "驾驶说明")
    private String driveRemark;

    /**
     * 加油说明
     */
    @ApiModelProperty(value = "加油说明")
    private String oilRemark;

    /**
     * 加油前公里数
     */
    @ApiModelProperty(value = "加油前公里数")
    private Integer mileageBefore;

    /**
     * 加油后公里数
     */
    @ApiModelProperty(value = "加油后公里数")
    private Integer mileageAfter;

    /**
     * 加油前余额
     */
    @ApiModelProperty(value = "加油前余额")
    private BigDecimal balanceBefore;

    /**
     * 加油后余额
     */
    @ApiModelProperty(value = "加油后余额")
    private BigDecimal balanceAfter;

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
     * 相关车辆ID
     */
    @ApiModelProperty(value = "相关车辆ID")
    private Long busId;

    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    private String numberPlate;

    /**
     * 车架号/车架号
     */
    @ApiModelProperty(value = "车架号/车架号")
    private String busCode;

    /**
     * dvr编号
     */
    @ApiModelProperty(value = "dvr编号")
    private String dvrCode;


}
