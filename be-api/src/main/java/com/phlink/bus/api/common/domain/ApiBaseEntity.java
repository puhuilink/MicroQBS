package com.phlink.bus.api.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ApiBaseEntity implements Serializable {

    @NotNull(message = "{required}", groups = {OnCheckID.class})
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(hidden = true,value = "删除标识")
    @JsonIgnore
    @TableLogic
    private Boolean deleted;
}
