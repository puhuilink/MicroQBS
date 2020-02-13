package com.phlink.bus.api.common.task;

import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 主要用于定时删除 Redis中 key为 api.user.active 中
 * 已经过期的 score
 */
@Slf4j
@Component
public class CacheTask {

    @Autowired
    private RedisService redisService;

    @Async
    @Scheduled(fixedRate = 3600000)
    public void run() {
        try {
            String now = DateUtil.formatFullTime(LocalDateTime.now());
            redisService.zremrangeByScore(BusApiConstant.ACTIVE_USERS_ZSET_PREFIX, "-inf", now);
            log.info("delete expired user");
        } catch (Exception ignore) {
        }
    }
}
