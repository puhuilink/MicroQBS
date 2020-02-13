package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dvr_camera_config")
public class DvrCameraConfig extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    private String dvrCode;

    /**
     * 通道编号
     */
    @ApiModelProperty(value = "通道编号")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Integer channelCode;

    /**
     * rtmp地址路由
     */
    @ApiModelProperty(value = "rtmp地址路由")
    private String url;

    /**
     * 位置编号
     */
    @ApiModelProperty(value = "位置编号")
    @NotBlank(message = "{required}", groups = {OnAdd.class})
    private String locationCode;

    /**
     * 绑定的车辆ID
     */
    @ApiModelProperty(value = "绑定的车辆ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long busId;

    @ApiModelProperty(value = "DVR服务器ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private transient Long dvrServerId;
}
