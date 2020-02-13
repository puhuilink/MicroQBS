package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.notify.event.BusStopEnterEvent;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.domain.Trip;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.route.service.IStopTimeService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BusStopEnterSendMsgToGuardianListener implements ApplicationListener<BusStopEnterEvent> {
    @Autowired
    private MqService mqService;
    @Autowired
    private IBusService busService;
    @Autowired
    private UserService userService;
    @Autowired
    private IStopTimeService stopTimeService;
    @Autowired
    private IStopService stopService;
    @Autowired
    private ITripService tripService;
    @Autowired
    private IRouteOperationService routeOperationService;

    @Async
    @Override
    public void onApplicationEvent(BusStopEnterEvent event) {
        String busCode = event.getBusCode();
        Long stopId = event.getStopId();
        Bus bus = busService.getByBusCode(busCode);
        if(bus == null) {
            log.error("校车[{}]不存在", busCode);
            return;
        }
        // 获得该站点
        Stop stop = stopService.getById(stopId);
        if(stop == null) {
            log.error("站点[{}]不存在", stopId);
            return;
        }
        // 获取当前校车线路上的所有家长的User信息
        List<User> guardians = userService.listOnRouteByBusId(bus.getId());
        // 获取路线的家长的设备ID
        List<String> registerIds = guardians.stream().map(User::getRegistrationId).filter(Objects::nonNull).collect(Collectors.toList());
        // 获得该站点的下一站
        RouteOperation routeOperation = routeOperationService.getByBusId(bus.getId());
        Trip trip = tripService.getNowTripForRouteOperation(routeOperation.getId());
        if(trip == null) {
            log.error("车辆[{}]没有进行中的行程", bus.getNumberPlate());
            return;
        }
        StopTime stopTime = stopTimeService.getNext(stopId, trip.getId());
        if(stopTime == null) {
            log.error("站点[{}]没有下一站，或下一站是学校", stop.getStopName());
            return;
        }
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        pushBean.setAlert(String.format("中益安达校车%s开始出发，请%s的家长做好接送准备。", bus.getNumberPlate(), stopTime.getStopName()));
        pushBean.setTitle("站点通知");
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);

        mqService.pushMsg(jiGuangPushBean);
    }
}
