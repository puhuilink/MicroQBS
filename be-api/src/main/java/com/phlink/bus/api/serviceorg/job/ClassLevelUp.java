package com.phlink.bus.api.serviceorg.job;

import com.phlink.bus.api.serviceorg.service.IClassesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClassLevelUp {

    @Autowired
    private IClassesService classesService;

    @Async
    @Scheduled(cron = "0 0 0 1 9 *")
    public void classLevelUp() {
        log.info("年级升级定时任务开始-------------------");
        long startTime = System.currentTimeMillis(); //获取开始时间
        this.classesService.gradeUp();
        long endTime = System.currentTimeMillis(); //获取结束时间
        log.info("年级升级定时任务结束-------------------");
        log.info("任务耗时:" + (endTime - startTime) + "ms");
    }
}
