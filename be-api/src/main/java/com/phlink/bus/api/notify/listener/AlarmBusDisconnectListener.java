package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.AlarmBusDisconnectEvent;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class AlarmBusDisconnectListener implements ApplicationListener<AlarmBusDisconnectEvent> {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MqService mqService;
    @Autowired
    private UserService userService;
    @Autowired
    private IBusService busService;

    @Async
    @Override
    public void onApplicationEvent(AlarmBusDisconnectEvent event) {
        String busCode = event.getBusCode();
        if(StringUtils.isBlank(busCode)) {
            return;
        }
        // 车辆所在车队队长
        List<User> user = userService.listBusLeaderByBus(busCode);

        Bus bus = busService.getByBusCode(busCode);
        User driver = userService.getDriverByBusCode(busCode);

        List<String> registerIds = user.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        pushBean.setTitle("设备失联");
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);


        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(new Date());

        pushBean.setAlert(String.format("中益安达校车%s，司机：%s 在%s发生监控设备失联事件，请悉知。", bus.getNumberPlate(), driver.getRealname(), dateText));
        mqService.pushMsg(jiGuangPushBean);
    }
}
