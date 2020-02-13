package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.notify.event.ImGroupStudentEnterEvent;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.common.Constants;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImGroupStudentEnterPushMsgListener implements ApplicationListener<ImGroupStudentEnterEvent> {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MqService mqService;

    private static final String MSG = "尊敬的%s，您已被添加为%s的主责任人，请您尽快下载“中益安达校车家长版APP”（下载安卓版、下载苹果版），感谢您和我们共同努力保障孩子人身安全，见证孩子健康成长！";

    @Async
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
        // 给每一个家长推送消息
        for(Long studentId : studentIds) {
            Student student = studentService.getStudentDetail(studentId);
            if(student == null) {
                continue;
            }
            // 写入redis
            RMap<String, String> rmap = redissonClient.getMap(Constants.BIND_GUARDIAN_CACHE_PREFIX + student.getMainGuardianMobile());
            rmap.put("realname", student.getMainGuardianName());
            rmap.put("studentName", student.getName());
            mqService.sendSmsMsg(Constants.MSG_TYPE_BIND_GUARDIAN, student.getMainGuardianMobile());
        }
    }
}
