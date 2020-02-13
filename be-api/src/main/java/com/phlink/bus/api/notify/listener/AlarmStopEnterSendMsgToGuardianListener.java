package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.AlarmStopEnterEvent;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AlarmStopEnterSendMsgToGuardianListener implements ApplicationListener<AlarmStopEnterEvent> {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MqService mqService;
    @Autowired
    private UserService userService;
    @Autowired
    private ITripService tripService;
    @Autowired
    private IGuardianService guardianService;

    @Async
    @Override
    public void onApplicationEvent(AlarmStopEnterEvent event) {
        StopTimeStudentDetail bindBusStoptimeDetailInfo = event.getStopTime();
        StopTimeStudentDetail nextBindBusStoptimeDetailInfo = event.getNextStopTime();
        TripState tripState = event.getTripState();
        if (bindBusStoptimeDetailInfo == null) {
            return;
        }

        if (nextBindBusStoptimeDetailInfo == null) {
            log.error("AlarmStopEnterEvent 下一站nextStopTime为空");
            return;
        }

        if (nextBindBusStoptimeDetailInfo.getSchoolStop()) {
            // 如果下站是学校
            return;
        }

        RList<StudentGuardianInfo> nextStopGuardians = tripService.getAllStudentList(tripState.getBusDetailInfo().getBusCode(), nextBindBusStoptimeDetailInfo.getStopTimeId());
        // 获取trip上的所有家长的User信息
//        List<StudentGuardianInfo> nextStopGuardians = nextBindBusStoptimeDetailInfo.getStudents();
        List<Guardian> allGuardians = new ArrayList<>();
        for (StudentGuardianInfo s : nextStopGuardians) {
            List<Guardian> guardianList = guardianService.listByStudentId(s.getStudentId());
            allGuardians.addAll(guardianList);
        }
        if(allGuardians.isEmpty()) {
            return;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(User::getUserId, allGuardians.stream().map(Guardian::getUserId).collect(Collectors.toList()));
        List<User> allUsers = userService.list(queryWrapper);
        // 获取路线的家长的设备ID
        List<String> registerIds = allUsers.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        if(registerIds.isEmpty()) {
            return;
        }
        log.info("Event {} 到达站点，发送消息给下一站家长[{}] registerIds[{}] 行程信息[{}]", event.getClass().getName(), JSON.toJSONString(allUsers), JSON.toJSONString(registerIds), JSON.toJSONString(tripState));

        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        pushBean.setTitle("到达站点");
        pushBean.setAlert(String.format("中益安达校车%s已到达%s，请%s的家长做好接送准备。", tripState.getBusDetailInfo().getNumberPlate(), bindBusStoptimeDetailInfo.getStopName(), nextBindBusStoptimeDetailInfo.getStopName()));
        mqService.pushMsg(jiGuangPushBean);

    }
}
