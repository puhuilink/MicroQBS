package com.phlink.bus.api.fence.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
* 区域围栏生效时间段
*
* @author wen
*/
@Data
@TableName("t_fence_time")
public class FenceTime {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 起始时间
     */
    @NotNull(message = "{required}")
    @ApiModelProperty(value = "起始时间")
    private LocalTime startTime;

    /**
     * 截止时间
     */
    @NotNull(message = "{required}")
    @ApiModelProperty(value = "截止时间")
    private LocalTime endTime;

    /**
     * 关联围栏ID
     */
    @ApiModelProperty(value = "关联围栏ID")
    private Long fenceId;


}
