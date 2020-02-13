package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.notify.event.ImGroupStudentEnterEvent;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImGroupStudentEnterAddGroupListener implements ApplicationListener<ImGroupStudentEnterEvent> {

    @Autowired
    private IGuardianService guardianService;

    @Override
    public void onApplicationEvent(ImGroupStudentEnterEvent event) {

        List<Long> studentIds = event.getStudentIds();
        Long routeOperationId = event.getRouteOperationId();
        if(routeOperationId == null) {
            return;
        }
        if(studentIds == null || studentIds.isEmpty()) {
            return;
        }
        // 1.添加学生的主责及监护人到对应的群组
        List<Long> guardianIdList = new ArrayList<>();
        for (Long studentId : studentIds) {
            List<Guardian> guardians = guardianService.listOtherGuardian(studentId);
            List<Long> gids = guardians.stream().map(Guardian::getUserId).distinct().collect(Collectors.toList());
            guardianIdList.addAll(gids);
        }

        // 2.将家长加到路线的群组
        guardianService.intoGroup(routeOperationId, guardianIdList.toArray(new Long[0]));
    }
}
