package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
* 
*
* @author wen
*/
@Data
@Accessors(chain = true)
@TableName("t_dvr_server")
public class DvrServer {

    @NotNull(message = "{required}", groups = {OnCheckID.class})
    @ApiModelProperty(value = "服务器IP或域名")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 服务器IP或域名
     */
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty(value = "服务器IP或域名")
    private String host;

    /**
     * 端口号
     */
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty(value = "端口号")
    private String port;

    /**
     * 功能描述
     */
    @Size(max = 1000, message = "{noMoreThan}")
    @ApiModelProperty(value = "功能描述")
    private String description;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

}
