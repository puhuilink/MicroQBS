package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.notify.event.ImGroupStudentOutEvent;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class ImGroupStudentOutListener implements ApplicationListener<ImGroupStudentOutEvent> {

    @Autowired
    private IImGroupsService groupsService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IGuardianService guardianService;

    @Override
    public void onApplicationEvent(ImGroupStudentOutEvent event) {

        List<Long> studentIds = event.getStudentIds();
        Long routeOperationId = event.getRouteOperationId();
        if(routeOperationId == null) {
            return;
        }
        if(studentIds == null || studentIds.isEmpty()) {
            return;
        }
        // 添加学生的主责从群组删除
        Collection<Student> students = studentService.listByIds(studentIds);
        List<Long> guardianIdList = new ArrayList<>();
        for (Student s : students) {
            // 其他监护人
            List<Guardian> guardianList = guardianService.listOtherGuardian(s.getId());
            for(Guardian guardian : guardianList) {
                //是否还有其他孩子在同一个路线关联中
                List<Student> allStudents = studentService.listByGuardianInRouteOperation(guardian.getUserId(), routeOperationId);
                if(allStudents.size() > 1) {
                    continue;
                }
                guardianIdList.add(guardian.getUserId());
            }

        }
        // 将家长从路线的群组移除
        ImGroups groups = groupsService.getByDepartId(routeOperationId);
        try {
            groupsService.remove(groups.getGroupId(), guardianIdList.toArray(new Long[0]));
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }
}
