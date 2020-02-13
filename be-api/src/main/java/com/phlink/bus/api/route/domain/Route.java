package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
* 路线
*
* @author wen
*/
@ApiModel(value = "路线对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_route")
@Excel("路线对象")
public class Route extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Contains the full name of a route. This name is generally more descriptive than the name from route_short_name and often includes the route's destination or stop.

        At least one of route_short_name or route_long_name must be specified, or both if appropriate. If the route has no long name, specify a route_short_name and use an empty string as the value for this field.
     */
    @ExcelField(value = "路线名称")
    @ApiModelProperty(value = "路线名称")
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class, OnUpdate.class})
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private String routeName;

    /**
     * 路线描述
     */
    @ApiModelProperty(value = "路线描述")
    private String routeDesc;

    /**
     * 路线类型
     */
    @ExcelField(value = "路线类型", writeConverterExp = "ONE_WAY=单程,ROUND_TRIP=往返,TWO_ROUND_TRIP=往返2次")
    @ApiModelProperty(value = "路线类型")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private RouteTypeEnum routeType;

    @ExcelField(value = "创建时间", writeConverter = LocalDateTimeConverter.class)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private Long createBy;

    @ApiModelProperty(hidden = true)
    private LocalDateTime modifyTime;

    @ApiModelProperty(hidden = true)
    private Long modifyBy;

    /**
     * 所属学校
     */
    @ApiModelProperty(value = "所属学校")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long schoolId;

    @ApiModelProperty(value = "轨迹ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long trajectoryId;

    @ExcelField(value = "出站时间(始发时间)")
    @ApiModelProperty(value = "出站时间(始发时间)")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime outboundTime;

    /**
     * 站点名称
     */
    @ApiModelProperty(value = "站点名称")
    private transient String stopName;

    /**
     * 学校名称
     */
    @ExcelField(value = "所属学校")
    @ApiModelProperty(value = "学校名称")
    private transient String schoolName;


}
