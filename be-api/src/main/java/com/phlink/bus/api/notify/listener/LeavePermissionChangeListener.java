package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.LeavePermissionChangeEvent;
import com.phlink.bus.api.notify.event.StudentAttendanceEvent;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LeavePermissionChangeListener implements ApplicationListener<LeavePermissionChangeEvent> {

    @Autowired
    private UserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private MqService mqService;

    @Async
    @Override
    public void onApplicationEvent(LeavePermissionChangeEvent event) {
        Long guardianId = event.getGuardianId();
        Long studentId = event.getStudentId();
        Long mainId = event.getMainId();
        Boolean isLeavePermission = event.getIsLeavePermission();

        Student student = studentService.getById(studentId);
        if(student == null) {
            log.error("学生[{}]不存在", studentId);
            return;
        }
        User user = userService.getById(guardianId);
        if(user == null) {
            log.error("监护人[{}]不存在", guardianId);
            return;
        }
        User main = userService.getById(mainId);
        if(main == null) {
            log.error("主责人[{}]不存在", mainId);
            return;
        }
        List<String> registerIds = Collections.singletonList(user.getRegistrationId());
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        pushBean.setTitle("请假权限更新");
        if(isLeavePermission) {
            pushBean.setAlert(String.format("主责任人%s给予您给孩子%s校车请假权限，可在APP首页“一键请假”帮孩子请假。", main.getRealname(), student.getName()));
        }else{
            pushBean.setAlert(String.format("主责任人%s已收回您给孩子%s校车请假的权限，请悉知。", main.getRealname(), student.getName()));
        }
        mqService.pushMsg(jiGuangPushBean);

    }
}
