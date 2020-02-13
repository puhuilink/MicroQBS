package com.phlink.bus.api.serviceorg.domain.VO;

import com.phlink.bus.api.serviceorg.domain.enums.ServiceStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentViewVO {

    @ApiModelProperty("学生id")
    private Long id;
    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String name;
    /**
     * 服务状态
     */
    @ApiModelProperty("服务状态")
    private ServiceStatusEnum serviceStatus;
    /**
     * 学校名称
     */
    @ApiModelProperty("学校名称")
    private String schoolName;
    /**
     * 入学年份
     */
    @ApiModelProperty("入学年份")
    private Integer enrollYear;
    /**
     * 班级
     */
    @ApiModelProperty("班级")
    private Integer classLevel;
    /**
     * 责任人电话
     */
    @ApiModelProperty("责任人电话")
    private String mobile;
    /**
     * 路线id
     */
    @ApiModelProperty("路线id")
    private Long routeId;
    /**
     * 站点id
     */
    @ApiModelProperty("站点id")
    private Long stopId;
    /**
     * 站点名称
     */
    @ApiModelProperty("站点名称")
    private String stopName;

    @ApiModelProperty("路线关联id")
    private Long routeOperationId;

    @ApiModelProperty("年级")
    private Integer grade;
}
