package com.phlink.bus.core.iotdata.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.phlink.bus.core.iotdata.service.DeviceInfoService;
import com.rabbitmq.client.Channel;
import io.rpc.core.device.EWatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Slf4j
@Component
public class QueueListener {

    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * @Description: {"batteryLevel":4,"deviceId":"3G*9612481148","direction":0,"latitude":39.972173,"longitude":116.37442,"speed":0,"timestamp":1568088107116}
     * @Param: [object, channel, message]
     * @Return: void
     * @Author wen
     * @Date 2019-09-10 12:04
     */
    @RabbitListener(queues = "mq.iot.positionInfo")
    @RabbitHandler
    public void process(Object object, Channel channel, Message message) throws Exception {
        try {
            String msg = new String(message.getBody());
//        System.out.println("client msg is" + msg);
//        log.info("client msg is" + msg);
            JSONObject jo = JSON.parseObject(msg);

            EWatchInfo eWatchInfo = EWatchInfo.newBuilder()
                    .setTimestamp(jo.getLongValue("timestamp"))
                    .setBatteryLevel(jo.getIntValue("batteryLevel"))
                    .setDeviceId(jo.getString("deviceId"))
                    .setDirection(jo.getIntValue("direction"))
                    .setLatitude(jo.getDoubleValue("latitude"))
                    .setLongitude(jo.getDoubleValue("longitude"))
                    .setSpeed(jo.getIntValue("speed"))
                    .build()
                    ;
            // 保存至redis
            deviceInfoService.saveDeviceInfo(eWatchInfo);
            // 保存至数据库

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
