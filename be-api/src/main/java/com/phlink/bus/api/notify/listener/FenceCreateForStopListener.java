package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.notify.event.FenceCreateEvent;
import com.phlink.bus.api.route.service.IStopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FenceCreateForStopListener implements ApplicationListener<FenceCreateEvent> {

    @Autowired
    private IStopService stopService;
    @Autowired
    private IFenceService fenceService;

    @Async
    @Override
    public void onApplicationEvent(FenceCreateEvent event) {
        FenceVO vo = event.getVo();
        if(vo == null) {
            return;
        }
        if(vo.getStopFence() == null || !vo.getStopFence()) {
            // 不需要创建站点围栏
            return;
        }
        if(vo.getRelationId() == null) {
            log.error("路线围栏关联ID为空，无法创建站点围栏");
            return;
        }
        //TODO 不需要创建站点围栏
//        List<FenceStopVO> list = stopService.listUnbindFenceStopInfo(vo.getRelationId());
//        list.forEach(fenceStopVO -> fenceService.createStopFence(fenceStopVO, vo));
    }
}
