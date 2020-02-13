package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.AddOtherGuardianEvent;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddOtherGuardianSendSmsListener implements ApplicationListener<AddOtherGuardianEvent> {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MqService mqService;

    @Async
    @Override
    public void onApplicationEvent(AddOtherGuardianEvent event) {
        String mobile = event.getMobile();
        String realname = event.getRealname();
        String studentName = event.getStudentName();
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        if (StringUtils.isBlank(realname)) {
            return;
        }
        if (StringUtils.isBlank(studentName)) {
            return;
        }
        // 写入redis
        RMap<String, String> rmap = redissonClient.getMap(Constants.ADD_GUARDIAN_CACHE_PREFIX + mobile);
        rmap.put("realname", realname);
        rmap.put("studentName", studentName);
        mqService.sendSmsMsg(Constants.MSG_TYPE_ADD_GUARDIAN, mobile);
    }
}
