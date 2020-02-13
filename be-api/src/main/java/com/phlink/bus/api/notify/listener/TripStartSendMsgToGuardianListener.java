package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.TripStartEvent;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
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
public class TripStartSendMsgToGuardianListener implements ApplicationListener<TripStartEvent> {
    @Autowired
    private MqService mqService;
    @Autowired
    private UserService userService;
    @Autowired
    private ITripService tripService;
    @Autowired
    private IGuardianService guardianService;
    @Autowired
    private IStudentService studentService;

    @Async
    @Override
    public void onApplicationEvent(TripStartEvent event) {
        TripState tripState = event.getTripState();
        if(tripState == null) {
            return;
        }
        // 获得该行程的第一站
        List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
        if(stopTimes == null || stopTimes.isEmpty()) {
            log.error("Event {}【行程开始】站点时刻为空, tripId={}", event.getClass().getName(), tripState.getId());
            return;
        }
        StopTimeStudentDetail schoolStop = null;
        if (TripRedirectEnum.GO.equals(tripState.getDirectionId())) {
            schoolStop = stopTimes.get(stopTimes.size() - 1);
        } else if (TripRedirectEnum.BACK.equals(tripState.getDirectionId())) {
            schoolStop = stopTimes.get(0);
        } else {
            log.error("Event {}行程方向错误，不是 GO 和 BLACK，tripState.getDirectionId() = {}", event.getClass().getName(), tripState.getDirectionId());
            return;
        }
        RList<StudentGuardianInfo> allStudents = tripService.getAllStudentList(tripState.getBusDetailInfo().getBusCode(), schoolStop.getStopTimeId());

        StopTimeStudentDetail nextStop = stopTimes.get(0);
        boolean isSchoolStop = tripService.checkSchoolStop(tripState, stopTimes, nextStop);
        if(isSchoolStop) {
            nextStop = stopTimes.get(1);
        }
        List<Guardian> allGuardians = new ArrayList<>();
        for (StudentGuardianInfo s : allStudents) {
            List<Guardian> guardianList = guardianService.listByStudentId(s.getStudentId());
            allGuardians.addAll(guardianList);
        }
        if(allGuardians.isEmpty()) {
            return;
        }

        // 获取trip上的所有家长的User信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(User::getUserId, allGuardians.stream().map(Guardian::getUserId).collect(Collectors.toList()));
        List<User> allUsers = userService.list(queryWrapper);
        // 获取路线的家长的设备ID
        List<String> registerIds = allUsers.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        if(registerIds.isEmpty()) {
            return;
        }
        log.info("Event {} 行程开始，通知行程的所有家长[{}] registerIds[{}] 行程信息[{}]", event.getClass().getName(), JSON.toJSONString(allUsers), JSON.toJSONString(registerIds), JSON.toJSONString(tripState));

        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        pushBean.setAlert(String.format("中益安达校车%s开始出发，请%s的家长做好接送准备。", tripState.getBusDetailInfo().getNumberPlate(), nextStop.getStopName()));
        pushBean.setTitle("校车出发");
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        mqService.pushMsg(jiGuangPushBean);
    }
}
