package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.route.controller.validation.OnPointStop;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* 
*
* @author wen
*/
@ApiModel(value = "站点对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_stop")
public class Stop extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "路线ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long routeId;

    @ApiModelProperty(value = "站点名称")
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class, OnUpdate.class})
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "站名")
    private String stopName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @DecimalMax("1000.000000")
    @DecimalMin("0.000001")
    @Digits(integer=3,fraction=6)
    @NotNull(message = "{required}", groups = {OnPointStop.class})
    @ExcelField(value = "经度")
    private BigDecimal stopLon;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @DecimalMax("1000.000000")
    @DecimalMin("0.000001")
    @Digits(integer=3,fraction=6)
    @NotNull(message = "{required}", groups = {OnPointStop.class})
    @ExcelField(value = "纬度")
    private BigDecimal stopLat;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;

    @ApiModelProperty(value = "站点描述")
    @Size(max = 200, message = "{noMoreThan}")
    @ExcelField(value = "站点描述")
    private String stopDesc;

    @ApiModelProperty(value = "站点在路线中的顺序")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Integer stopSequence;

    /**
     * 排序字段
     */
    private transient String sortField;

    /**
     * 排序规则 ascend 升序 descend 降序
     */
    private transient String sortOrder;

    /**
     * 创建时间--开始时间
     */
    private transient Long createTimeFrom;

    /**
     * 创建时间--结束时间
     */
    private transient Long createTimeTo;


}
