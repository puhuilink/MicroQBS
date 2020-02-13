package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.notify.event.ImGroupCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImGroupCreateListener implements ApplicationListener<ImGroupCreateEvent> {

    @Autowired
    private IImGroupsService groupsService;

    @Override
    public void onApplicationEvent(ImGroupCreateEvent imGroupCreateEvent) {
        ImGroups imGroups = imGroupCreateEvent.getImGroups();
        Long[] members = imGroupCreateEvent.getMemberIds();
        if(imGroups == null) {
            return;
        }
        try {
            groupsService.createImGroups(imGroups);
            // 将司机加入群组
            if(members == null || members.length == 0) {
                return;
            }
            groupsService.invite(imGroups.getGroupId(), members);
        } catch (RedisConnectException e) {
            log.error("创建群组失败", e);
        }
    }
}
