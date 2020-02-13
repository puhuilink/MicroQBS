package com.phlink.bus.api.common.lisenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.service.IEwatchLocationService;
import com.phlink.bus.api.map.response.AmapCoordinateResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.notify.event.EwatchLocationEvent;
import com.phlink.bus.common.Constants;
import com.rabbitmq.client.Channel;
import io.rpc.core.device.EWatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class IotQueueListener {

    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IEwatchLocationService ewatchLocationService;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private ApplicationContext context;

    /**
     * @Description: {"batteryLevel":4,"deviceId":"3G*9612481148","direction":0,"latitude":39.972173,"longitude":116.37442,"speed":0,"timestamp":1568088107116}
     * @Param: [object, channel, message]
     * @Return: void
     * @Author wen
     * @Date 2019-09-10 12:04
     */
    @RabbitListener(queues = Constants.MQ_QUEUE_SEND_EWATCH)
    @RabbitHandler
    public void process(Object object, Channel channel, Message message) throws Exception {
        try {
            String msg = new String(message.getBody());
//        System.out.println("client msg is" + msg);
            log.info("client msg is" + msg);
            JSONObject jo = JSON.parseObject(msg);

            // 转高德坐标
            AmapCoordinateResultEntity entity = mapAmapService.convertGpsCoordsys(jo.getString("longitude"), jo.getString("latitude"));
            List<Double[]> points = entity.getPoints();
            if(points == null || points.isEmpty()) {
                return;
            }
            Double[] one = points.get(0);

            EWatchInfo eWatchInfo = EWatchInfo.newBuilder()
                    .setTimestamp(jo.getLongValue("timestamp"))
                    .setBatteryLevel(jo.getIntValue("batteryLevel"))
                    .setDeviceId(jo.getString("deviceId"))
                    .setDirection(jo.getIntValue("direction"))
                    .setLongitude(one[0])
                    .setLatitude(one[1])
                    .setSpeed(jo.getIntValue("speed"))
                    .build()
                    ;
            // 保存至redis
            cacheDeviceInfoService.saveDeviceInfo(eWatchInfo);
            // 保存至数据库
            EwatchLocation ewatchLocation = ewatchLocationService.createEwatchLocation(eWatchInfo);
            // 异步处理
            context.publishEvent(new EwatchLocationEvent(this, ewatchLocation));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
