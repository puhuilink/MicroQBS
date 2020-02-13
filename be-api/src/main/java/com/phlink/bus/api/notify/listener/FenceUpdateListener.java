package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.FenceUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FenceUpdateListener implements ApplicationListener<FenceUpdateEvent> {

    @Autowired
    private IFenceService fenceService;
    @Autowired
    private IMapBaiduService mapBaiduService;
    @Autowired
    private IMapAmapService mapAmapService;

    @Async
    @Override
    public void onApplicationEvent(FenceUpdateEvent event) {
        FenceVO vo = event.getVo();
        if(vo == null) {
            return;
        }
        if(FenceTypeEnum.CIRCLE.equals(vo.getFenceType()) || FenceTypeEnum.POLYGON.equals(vo.getFenceType()) ) {
            try {
                this.mapAmapService.updateFence(vo);
            } catch (BusApiException e) {
                e.printStackTrace();
            }
        }else if(FenceTypeEnum.POLYLINE.equals(vo.getFenceType())) {
            try {
                this.mapBaiduService.updatepolylinefence(vo);
            } catch (BusApiException e) {
                e.printStackTrace();
            }
        }else{
            log.error("无法识别电子围栏类型，fenceType：" + vo.getFenceType());
            return;
        }

//        if(vo.getJobs() != null) {
//            List<FenceCronJob> jobs = fenceService.saveFenceJob(vo.getJobs().toJSONString(), vo.getFenceId());
//            fenceService.updateFenceJob(vo.getId(), jobs);
//        }
    }
}
