package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
* dvr设备
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dvr")
public class Dvr extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    public static final Integer CHANNEL_NUMBER = 8;

    /**
     * 通道数量
     */
    @ApiModelProperty(value = "通道数量")
    private Integer channelNumber;

    /**
     * dvr设备编号
     */
    @ApiModelProperty(value = "dvr设备编号")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private String dvrCode;

    private LocalDateTime createTime;

    private Long createBy;

    /**
     * 绑定车辆ID
     */
    @ApiModelProperty(value = "绑定车辆ID")
    private Long busId;
    /**
     * 绑定服务器ID
     */
    @ApiModelProperty(value = "绑定服务器ID")
    private Long dvrServerId;
    /**
     * 在线状态
     */
    @ApiModelProperty(value = "online")
    private Integer online;


}
