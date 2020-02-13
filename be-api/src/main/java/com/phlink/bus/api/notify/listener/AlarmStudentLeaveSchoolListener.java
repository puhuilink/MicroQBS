package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.device.domain.DeviceRelation;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.service.IDeviceRelationService;
import com.phlink.bus.api.notify.event.AlarmStudentLeaveSchoolEvent;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
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
public class AlarmStudentLeaveSchoolListener implements ApplicationListener<AlarmStudentLeaveSchoolEvent> {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MqService mqService;
    @Autowired
    private UserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IDeviceRelationService deviceRelationService;

    @Async
    @Override
    public void onApplicationEvent(AlarmStudentLeaveSchoolEvent event) {
        EwatchLocation location = event.getLocation();
        if (location == null) {
            return;
        }
        // 设备关联的学生
        DeviceRelation dr = deviceRelationService.getByDeviceCode(location.getDeviceId());
        if(dr == null) {
            log.error("[AlarmStudentLeaveSchoolEvent] 设备{}未找到学生关联信息", location.getDeviceId());
            return;
        }
        Student student = studentService.getById(dr.getStudentId());
        if(student == null) {
            log.error("[AlarmStudentLeaveSchoolEvent] 设备{}未找到关联学生", location.getDeviceId());
            return;
        }
        // 学生的监护人
        List<User> user = userService.listGuardianByStudent(dr.getStudentId());

        List<String> registerIds = user.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        pushBean.setTitle("异常离校");
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(new Date());

        pushBean.setAlert(String.format("%s于%s发生异常离校事件，请家长及时联系学校，了解具体情况，感谢您的配合。", student.getName(), dateText));

        mqService.pushMsg(jiGuangPushBean);
    }
}
