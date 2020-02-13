package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.domain.FenceCronJob;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.job.service.JobService;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.FenceDeleteEvent;
import com.phlink.bus.api.route.service.IStopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FenceDeleteListener implements ApplicationListener<FenceDeleteEvent> {

    @Autowired
    private IStopService stopService;
    @Autowired
    private IFenceService fenceService;
    @Autowired
    private IMapBaiduService mapBaiduService;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private JobService jobService;

    @Async
    @Override
    public void onApplicationEvent(FenceDeleteEvent event) {
        List<Long> fenceIds = event.getFenceIds();
        if(fenceIds == null || fenceIds.isEmpty()) {
            return;
        }
        List<Fence> fences = this.fenceService.listByIdsIncludeDelete(fenceIds);

        List<Integer> baiduList = new ArrayList<>();
        List<String> amapList = new ArrayList<>();
        List<String> jobIdList = new ArrayList<>();
        for (Fence fence : fences) {
            if (fence.getFenceType() == FenceTypeEnum.POLYLINE) {
                try {
                    baiduList.add(Integer.parseInt(fence.getFenceId()));
                }catch (Exception ex) {
                    // 忽略
                }
                //获取站点列表
                List<String> stopFenceIdsList = fenceService.getStopFences(fence.getRelationId());
                amapList.addAll(stopFenceIdsList);
            } else {
                amapList.add(fence.getFenceId());
            }
            if(fence.getJobs() != null) {
                List<FenceCronJob> jobList = JSONObject.parseArray(fence.getJobs().toJSONString(), FenceCronJob.class);
                for (FenceCronJob fenceCronJob : jobList) {
                    if (fenceCronJob.getStartJobId() != null) {
                        jobIdList.add(fenceCronJob.getStartJobId().toString());
                    }
                    if (fenceCronJob.getStopJobId() != null) {
                        jobIdList.add(fenceCronJob.getStopJobId().toString());
                    }
                }
            }
        }
        //删除百度路线围栏
        try {
            if(!baiduList.isEmpty()) {
                mapBaiduService.deletepolylinefence(baiduList);
            }
        } catch (BusApiException e) {
            log.error(e.getMessage());
        }
        if(!jobIdList.isEmpty()) {
            //删除定时任务
            this.jobService.deleteJobs(jobIdList.toArray(new String[0]));
        }

        // 删除高德围栏
        amapList.forEach(fenceId -> {
            try {
                mapAmapService.deleteFence(fenceId);
            } catch (BusApiException e) {
                log.error(e.getMessage());
            }
        });
    }
}
