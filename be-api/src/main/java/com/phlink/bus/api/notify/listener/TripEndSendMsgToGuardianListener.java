package com.phlink.bus.api.notify.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.TripEndEvent;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
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
public class TripEndSendMsgToGuardianListener implements ApplicationListener<TripEndEvent> {
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
    public void onApplicationEvent(TripEndEvent event) {
        TripState tripState = event.getTripState();
        if(tripState == null) {
            return;
        }
        List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
        if(stopTimes == null || stopTimes.isEmpty()) {
            log.error("【行程结束】站点时刻为空, tripId={}", tripState.getId());
            return;
        }
        if (TripRedirectEnum.GO.equals(tripState.getDirectionId())) {
            //获取行程最后一站，学校
            StopTimeStudentDetail schoolStop = stopTimes.get(stopTimes.size() - 1);
            // 获取trip上的所有家长的User信息

            RList<StudentGuardianInfo> allStudents = tripService.getAllStudentList(tripState.getBusDetailInfo().getBusCode(), schoolStop.getStopTimeId());
            List<Guardian> allGuardians = new ArrayList<>();
            for (StudentGuardianInfo s : allStudents) {
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
            log.info("Event {} 行程结束，到达学校，通知行程的所有家长[{}] registerIds[{}] 行程信息[{}]", event.getClass().getName(), JSON.toJSONString(allUsers), JSON.toJSONString(registerIds), JSON.toJSONString(tripState));

            JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
            PushBean pushBean = new PushBean();
            pushBean.setAlert(String.format("中益安达校车%s已到达学校，并完成孩子和学校老师的交接，请悉知。", tripState.getBusDetailInfo().getNumberPlate()));
            pushBean.setTitle("行程结束");
            jiGuangPushBean.setPushBean(pushBean);
            jiGuangPushBean.setRegistrationIds(registerIds);
            mqService.pushMsg(jiGuangPushBean);
        }
    }
}
