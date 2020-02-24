package com.phlink.core.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phlink.core.web.common.validation.tag.OnCheckID;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BaseEntity {
    @NotNull(message = "{required}", groups = {OnCheckID.class})
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonIgnore
    @TableLogic
    private Boolean deleted;

    private LocalDateTime createTime;
    private Long createBy;
    private LocalDateTime modifyTime;
    private Long modifyBy;
}
