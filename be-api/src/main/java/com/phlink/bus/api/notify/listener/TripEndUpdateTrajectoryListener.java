package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.notify.event.TripEndEvent;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Component
public class TripEndUpdateTrajectoryListener implements ApplicationListener<TripEndEvent> {

    @Autowired
    private ITrajectoryService trajectoryService;

    @Autowired
    private IDvrLocationService dvrLocationService;

    @Async
    @Override
    public void onApplicationEvent(TripEndEvent event) {
        TripLog tripLog = event.getTripLog();
        if(tripLog == null) {
            return;
        }
        log.info("更新轨迹{}", tripLog);
        Trajectory trajectory = trajectoryService.getByTripLogId(tripLog.getId());
        trajectory.setEndTime(LocalDateTime.now());
        trajectory.setDuration(trajectory.getEndTime().toInstant(ZoneOffset.of("+8")).getEpochSecond() - trajectory.getStartTime().toInstant(ZoneOffset.of("+8")).getEpochSecond());
        // 获取这段时间内上传了多少个坐标点
        List<DvrLocation> locations = dvrLocationService.listByBusId(trajectory.getBusId(), trajectory.getStartTime(), trajectory.getEndTime());
        trajectory.setCounts((long) locations.size());
        trajectoryService.updateById(trajectory);
    }
}
