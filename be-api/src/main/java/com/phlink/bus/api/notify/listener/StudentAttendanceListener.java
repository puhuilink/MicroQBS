package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.StudentAttendanceEvent;
import com.phlink.bus.api.route.domain.StopAttendance;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.domain.enums.StopAttendanceStateEnum;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StudentAttendanceListener implements ApplicationListener<StudentAttendanceEvent> {
    @Autowired
    private MqService mqService;
    @Autowired
    private UserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IGuardianService guardianService;

    private static final String UP_BUS = "%s于%s打卡上校车(%s)，请悉知。";
    private static final String DOWN_BUS = "%s于%s打卡下校车(%s)，请悉知。";

    @Async
    @Override
    public void onApplicationEvent(StudentAttendanceEvent event) {
        StopAttendance stopAttendance = event.getStopAttendance();
        if(stopAttendance == null) {
            log.error("Event {} 参数 stopAttendance 不存在", event.getClass().getName());
            return;
        }
        TripState tripState = event.getTripState();
        // 获得该学生家长
        Student student = studentService.getById(stopAttendance.getStudentId());
        if(student == null) {
            log.error("Event {} 该学生ID[{}]不存在", event.getClass().getName(), stopAttendance.getStudentId());
            return;
        }
        List<Guardian> allGuardians = guardianService.listByStudentId(student.getId());
        if(allGuardians == null || allGuardians.isEmpty()) {
            log.error("Event {} 该学生[{}]家长不存在", event.getClass().getName(), stopAttendance.getStudentId());
            return;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(User::getUserId, allGuardians.stream().map(Guardian::getUserId).collect(Collectors.toList()));
        List<User> allUsers = userService.list(queryWrapper);
        // 获取家长的设备ID
        List<String> registerIds = allUsers.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());

        log.info("Event {} 学生打卡，通知学生的所有家长[{}] registerIds[{}] 行程信息[{}]", event.getClass().getName(), JSON.toJSONString(allUsers), JSON.toJSONString(registerIds), JSON.toJSONString(tripState));

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(new Date());

        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        if(StopAttendanceStateEnum.UP.getValue().equals(stopAttendance.getType())) {
            pushBean.setAlert(String.format(UP_BUS, student.getName(), dateText, tripState.getBusDetailInfo().getNumberPlate()));
            pushBean.setTitle("孩子已上车");
        }else if(StopAttendanceStateEnum.DOWN.getValue().equals(stopAttendance.getType())) {
            pushBean.setAlert(String.format(DOWN_BUS, student.getName(), dateText, tripState.getBusDetailInfo().getNumberPlate()));
            pushBean.setTitle("孩子已下车");
        }else{
            log.error("打卡类型错误 {}", JSON.toJSONString(stopAttendance));
            return;
        }
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        mqService.pushMsg(jiGuangPushBean);
    }
}
