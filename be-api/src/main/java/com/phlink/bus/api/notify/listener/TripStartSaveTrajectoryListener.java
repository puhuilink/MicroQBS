package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.notify.event.TripStartEvent;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TripStartSaveTrajectoryListener implements ApplicationListener<TripStartEvent> {
    @Autowired
    private ITrajectoryService trajectoryService;

    @Async
    @Override
    public void onApplicationEvent(TripStartEvent event) {
        TripState tripState = event.getTripState();
        if(tripState == null) {
            return;
        }
        TripLog tripLog = event.getTripLog();
        log.info("Event {}, 创建轨迹{}", event.getClass().getName(), JSON.toJSONString(tripState));
        // 保存一条轨迹
        Trajectory trajectory = new Trajectory();
        trajectory.setName(tripState.getBusDetailInfo().getNumberPlate());
        trajectory.setStartTime(LocalDateTime.now());
        // 行程相关信息
        trajectory.setTripLogId(tripLog.getId());
        trajectory.setTripId(tripState.getId());
        trajectory.setCreateBy(tripState.getBusDetailInfo().getBindBusTeacherId());
        trajectory.setRouteId(tripState.getBusDetailInfo().getRouteId());
        // 车辆相关信息
        trajectory.setTid(tripState.getBusDetailInfo().getTid());
        trajectory.setBusId(tripState.getBusDetailInfo().getId());
        trajectory.setBusCode(tripState.getBusDetailInfo().getBusCode());
        trajectory.setEngineModel(tripState.getBusDetailInfo().getEngineModel());
        trajectory.setNumberPlate(tripState.getBusDetailInfo().getNumberPlate());
        try {
            trajectoryService.createAndUpToMap(trajectory);
        } catch (BusApiException e) {
            log.error("[TripStartEvent] 轨迹创建失败",e);
        }
    }
}
