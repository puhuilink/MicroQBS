package com.phlink.bus.api.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    @ApiModelProperty(value = "每页数据条数",example = "10")
    private int pageSize = 10;
    @ApiModelProperty(value = "当前页",example = "1")
    private int pageNum = 1;
    @ApiModelProperty(value = "排序类型(升降序)")
    private String sortField;
    @ApiModelProperty(value = "排序字段")
    private String sortOrder;
}
