package com.phlink.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.core.web.base.PhlinkBaseEntity;
import com.phlink.core.base.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wen
 */
@Data
@TableName("t_dict_data")
@ApiModel(value = "字典")
public class DictData extends PhlinkBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据名称")
    private String title;

    @ApiModelProperty(value = "数据值")
    private String value;

    @ApiModelProperty(value = "排序值")
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "所属字典")
    private String dictId;
}
