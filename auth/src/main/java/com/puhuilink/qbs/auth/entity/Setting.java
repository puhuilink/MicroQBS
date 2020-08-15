package com.puhuilink.qbs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.core.common.base.QbsBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("qbs_setting")
@ApiModel(value = "配置")
@NoArgsConstructor
public class Setting extends QbsBaseEntity {

    @ApiModelProperty(value = "配置值value")
    private String value;

    public Setting(String id) {

        super.setId(id);
    }
}
