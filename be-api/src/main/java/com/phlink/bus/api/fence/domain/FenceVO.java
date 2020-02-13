package com.phlink.bus.api.fence.domain;

import com.alibaba.fastjson.JSONArray;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.fence.domain.enums.ConditionEnum;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.domain.enums.RelationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Validated
public class FenceVO extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 围栏名称
     */
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}")
    @ApiModelProperty("围栏名称")
    private String fenceName;
    /**
     * 围栏坐标点
     */
    @ApiModelProperty("围栏坐标点")
    private Double[][] vertexes;

    /**
     * 偏离距离（米）
     */
    @ApiModelProperty(value = "偏离距离（米）", example = "100")
    private Integer offsets;

    /**
     * 围栏类型(1:圆形，2：多边形，3：线型)
     */
    @NotNull(message = "{required}")
    @ApiModelProperty("围栏类型(1:圆形，2：多边形，3：线型)")
    private FenceTypeEnum fenceType;
    /**
     * 圆心
     */
    @ApiModelProperty("圆心")
    private String center;
    /**
     * 半径
     */
    @ApiModelProperty(value = "半径", example = "100")
    private Integer radius;

    /**
     * 触发动作
     */
    @ApiModelProperty(value = "触发动作", example = "ENTERORLEAVE")
    private ConditionEnum alertCondition;
    /**
     * 电子围栏id
     */
    @ApiModelProperty("电子围栏id")
    private String fenceId;

    @NotNull(message = "{required}")
    @ApiModelProperty(value = "关联id")
    private Long relationId;

    @ApiModelProperty("关联类型(1:学校，2:路线，3:站点)")
    private RelationTypeEnum relationType;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long modifyBy;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    /**
     * 定时任务json
     */
    @ApiModelProperty("定时任务json")
    private JSONArray jobs;

    /**
     * 一天内围栏监控时段
     */
    @ApiModelProperty("一天内围栏监控时段")
    private List<FenceTime> monitorTime;

    @ApiModelProperty("是否创建站点围栏")
    private Boolean stopFence;
}
