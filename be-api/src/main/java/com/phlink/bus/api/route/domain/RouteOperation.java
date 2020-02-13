package com.phlink.bus.api.route.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
* 路线关联表
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_route_operation")
@ApiModel("路线关联表")
@Excel("路线关联表")
public class RouteOperation extends ApiBaseEntity {

    /**
     * 绑定车辆ID
     */
    @ApiModelProperty(value = "绑定校车ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindBusId;

    /**
     * 绑定随车老师ID
     */
    @ApiModelProperty(value = "绑定随车老师ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindBusTeacherId;

    /**
     * 绑定司机ID
     */
    @ApiModelProperty(value = "绑定司机ID")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long bindDriverId;

    /**
     * 路线ID
     */
    @ApiModelProperty(value = "路线ID")
    @NotNull(message = "{required}", groups = {OnAdd.class})
    private Long routeId;

    @ExcelField(value = "路线名称")
    @ApiModelProperty(value = "路线名称")
    private transient String routeName;

    @ExcelField(value = "车架号")
    @ApiModelProperty(value = "车架号")
    private transient String busCode;

    @ExcelField(value = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private transient String numberPlate;

    @ExcelField(value = "司机名字")
    @ApiModelProperty(value = "司机名字")
    private transient String driverRealName;

    @ExcelField(value = "司机手机号")
    @ApiModelProperty(value = "司机手机号")
    private transient String driverMobile;

    @ExcelField(value = "随车老师名字")
    @ApiModelProperty(value = "随车老师名字")
    private transient String busTeacherRealName;

    @ExcelField(value = "随车老师手机号")
    @ApiModelProperty(value = "随车老师手机号")
    private transient String busTeacherMobile;

    @ExcelField(value = "学生数量")
    @ApiModelProperty(value = "学生数量")
    private transient Integer studentNum;

    @ApiModelProperty(value = "学校id")
    private transient String schoolId;

    @ExcelField(value = "学校名称")
    @ApiModelProperty(value = "学校名称")
    private transient String schoolName;

    @ExcelField(value = "路线创建时间")
    @ApiModelProperty(value = "路线创建时间")
    private transient LocalDateTime createTime;

    @ExcelField(value = "路线更新时间")
    @ApiModelProperty(value = "路线更新时间")
    private transient LocalDateTime modifyTime;

}
