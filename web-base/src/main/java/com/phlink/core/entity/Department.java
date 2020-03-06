package com.phlink.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.core.base.PhlinkBaseEntity;
import com.phlink.core.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("t_department")
@ApiModel(value = "部门")
public class Department extends PhlinkBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @TableField(exist=false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @TableField(exist=false)
    @ApiModelProperty(value = "主负责人")
    private List<String> mainMaster;

    @TableField(exist=false)
    @ApiModelProperty(value = "副负责人")
    private List<String> viceMaster;
}