package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.notify.event.FenceDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FenceDeleteForStopListener implements ApplicationListener<FenceDeleteEvent> {

    @Autowired
    private IFenceService fenceService;

    @Async
    @Override
    public void onApplicationEvent(FenceDeleteEvent event) {
        List<Long> fenceIds = event.getFenceIds();
        if(fenceIds == null || fenceIds.isEmpty()) {
            return;
        }
        List<Fence> fences = this.fenceService.listByIdsIncludeDelete(fenceIds);
        List<Long> routeIds = fences.stream().map(Fence::getRelationId).collect(Collectors.toList());
        // 删除围栏下的站点围栏
        fenceService.deleteStopFenceByRouteIds(routeIds);
    }
}
