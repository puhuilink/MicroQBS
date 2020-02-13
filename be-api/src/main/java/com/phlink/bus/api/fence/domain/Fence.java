package com.phlink.bus.api.fence.domain;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.fence.domain.enums.ConditionEnum;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.domain.enums.RelationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 电子围栏
 *
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_fence")
public class Fence extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 围栏名称
     */
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}")
    @ApiModelProperty("围栏名称")
    private String fenceName;

    /**
     * 终端
     */
    @ApiModelProperty(value = "终端", hidden = true)
    private String[] entityNames;

    /**
     * 围栏坐标点
     */
    @ApiModelProperty("围栏坐标点")
    private String vertexes;
    @ApiModelProperty("围栏坐标点数量")
    private Integer vertexesNum;

    /**
     * 偏离距离（米）
     */
    @ApiModelProperty(value = "偏离距离（米）", example = "1")
    private Integer offsets;

    /**
     * 围栏类型(1:圆形，2：多边形，3：线型)
     */
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
    @ApiModelProperty(value = "半径", example = "1")
    private Integer radius;

    /**
     * 触发动作
     */
    @ApiModelProperty("触发动作")
    private ConditionEnum alertCondition;
    /**
     * 电子围栏id
     */
    @ApiModelProperty("电子围栏id")
    private String fenceId;

    @ApiModelProperty(value = "关联id")
    private Long relationId;

    @ApiModelProperty("关联类型(1:学校，2:路线)")
    private RelationTypeEnum relationType;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", example = "1", hidden = true)
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", example = "1", hidden = true)
    private Long modifyBy;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", hidden = true)
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "高德围栏是否启用", hidden = true)
    private Boolean enabled;
    /**
     * 定时任务json
     */
    @ApiModelProperty("定时任务json")
    private JSONArray jobs;

    @ApiModelProperty("是否创建站点围栏")
    private Boolean stopFence;

    @ApiModelProperty("学校名称")
    private transient String schoolName;

    @ApiModelProperty("路线名称")
    private transient String routeName;
}