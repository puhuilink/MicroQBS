package com.phlink.qbs.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.qbs.core.web.base.PhlinkBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_setting")
@ApiModel(value = "配置")
@NoArgsConstructor
public class Setting extends PhlinkBaseEntity {

    @ApiModelProperty(value = "配置值value")
    private String value;

    public Setting(String id) {

        super.setId(id);
    }
}
