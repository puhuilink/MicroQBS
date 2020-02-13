package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_camera_location")
public class CameraLocation {

    private static final long serialVersionUID = 1L;

    /**
     * 摄像头位置编码
     */
    @ApiModelProperty(value = "摄像头位置编码")
    @TableId(value = "location_code", type = IdType.INPUT)
    private String locationCode;

    /**
     * 位置描述
     */
    @ApiModelProperty(value = "位置描述")
    private String locationDesc;

    /**
     * 是否对家长开放
     */
    @ApiModelProperty(value = "是否对家长开放")
    private Boolean guardianOpen;


}
