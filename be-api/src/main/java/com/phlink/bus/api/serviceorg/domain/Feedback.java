package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_feedback")
public class Feedback extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    private LocalDateTime createTime;

    /**
     * 反馈人
     */
    @ApiModelProperty(value = "反馈人")
    private Long userId;

    @ApiModelProperty(value = "姓名")
    private String realname;

    /**
     * 反馈人手机号
     */
    @ApiModelProperty(value = "反馈人手机号")
    private String mobile;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value = "反馈内容")
    private String content;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String state;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    private Long dealId;

    /**
     * 创建时间--开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private transient String createTimeFrom;

    /**
     * 创建时间--结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private transient String createTimeTo;


}
