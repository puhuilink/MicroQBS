package com.phlink.bus.api.bus.domain.VO;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DvrBusLocationInfoVO {
    private Long id;

    private String dvrCode;

    /**
     * 通道编号
     */
    @ApiModelProperty(value = "通道编号")
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
    private String locationCode;

    /**
     * 绑定的车辆ID
     */
    @ApiModelProperty(value = "车辆ID")
    private Long busId;

    @ApiModelProperty(value = "位置")
    private String locationDesc;

    @ApiModelProperty(value = "车架号")
    private String busCode;

    @ApiModelProperty(value = "车牌号")
    private String numberPlate;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "dvr视频服务器ID")
    private Long dvrServerId;

    @ApiModelProperty(value = "通道数量")
    private Integer channelNumber;
}
