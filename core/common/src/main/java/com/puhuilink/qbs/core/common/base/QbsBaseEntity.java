/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:22
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/base/PhlinkBaseEntity.java
 */
package com.puhuilink.qbs.core.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puhuilink.qbs.core.base.gson.annotation.Exclude;
import com.puhuilink.qbs.core.common.validate.tag.OnCheckID;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class QbsBaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(message = "{required}", groups = {OnCheckID.class})
    @ApiModelProperty(value = "唯一标识")
    private String id;

    @Exclude
    @TableLogic
    @ApiModelProperty(value = "删除标志 默认0")
    private Integer delFlag;

    @Exclude
    @Version
    @ApiModelProperty(value = "乐观锁")
    private Integer version;
}

