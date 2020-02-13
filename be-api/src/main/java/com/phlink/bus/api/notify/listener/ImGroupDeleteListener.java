package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.notify.event.ImGroupDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImGroupDeleteListener implements ApplicationListener<ImGroupDeleteEvent> {

    @Autowired
    private IImGroupsService groupsService;

    @Override
    public void onApplicationEvent(ImGroupDeleteEvent imGroupDeleteEvent) {
        String[] ids = imGroupDeleteEvent.getIds();
        if(ids == null || ids.length == 0) {
            return;
        }
        try {
            groupsService.deleteImGroupss(ids);
        } catch (RedisConnectException e) {
            log.error("删除群组失败", e);
        }
    }
}
