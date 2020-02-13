package com.phlink.bus.api.route.domain;

import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.route.domain.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Data
public class TripState implements Serializable {
    private Long id;
    @ApiModelProperty(value = "路线ID")
    private Long routeId;
    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
    @ApiModelProperty(value = "行程时间类型")
    private TripTimeEnum tripTime;
    @ApiModelProperty(value = "行程方向，1：去程； -1：返程")
    private TripRedirectEnum directionId;
    private String routeName;
    private RouteTypeEnum routeType;
    private LocalTime startTime;
    private LocalTime endTime;
    /**
     * 超时未完成 需要根据具体时间判断，可能暂时先不用
     */
    @ApiModelProperty(value = "运行状态, 1: 待运行，2：运行中，3：完成, 4: 超时未完成")
    private TripStateRunningEnum runningState;
    @ApiModelProperty(value = "行程打卡状态, 1: 待运行，2：运行中，3：完成")
    private TripLogRunningEnum logRunningState;

    @ApiModelProperty(value = "车辆详情")
    private BindBusDetailInfo busDetailInfo;

    @ApiModelProperty(value = "站点时刻详情列表")
    private List<StopTimeStudentDetail> stopTimes;

}
