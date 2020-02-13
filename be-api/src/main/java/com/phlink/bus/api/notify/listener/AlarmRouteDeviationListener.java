package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.AlarmRouteDeviationEvent;
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
public class AlarmRouteDeviationListener implements ApplicationListener<AlarmRouteDeviationEvent> {

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
    public void onApplicationEvent(AlarmRouteDeviationEvent event) {
        DvrLocation dvrLocation = event.getLocation();
        if(dvrLocation == null) {
            return;
        }
        // 车辆所在车队队长
        List<User> user = userService.listBusLeaderByBus(dvrLocation.getBusCode());

        Bus bus = busService.getByBusCode(dvrLocation.getBusCode());
        User driver = userService.getDriverByBusCode(dvrLocation.getBusCode());

        List<String> registerIds = user.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        pushBean.setTitle("偏离路线");
        Date date=new Date(dvrLocation.getGpstime());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);

        pushBean.setAlert(String.format("中益安达校车%s，司机：%s 在%s发生偏离路线事件，请悉知。", bus.getNumberPlate(), driver.getRealname(), dateText));

        mqService.pushMsg(jiGuangPushBean);
    }
}
