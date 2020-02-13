package com.phlink.bus.message.service;


import com.alibaba.fastjson.JSON;
import com.phlink.bus.common.Constants;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.message.jpush.service.JiGuangPushService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushJGService {

    @Autowired
    private JiGuangPushService jiGuangPushService;

    @RabbitListener(queues = Constants.MQ_QUEUE_SEND_PUSH)
    @RabbitHandler
    public void process(Object object, Channel channel, Message message) throws Exception {
        try {
            String msg = new String(message.getBody());
            log.info(msg);
            JiGuangPushBean bean = JSON.parseObject(msg, JiGuangPushBean.class);
            if(bean.getRegistrationIds() == null || bean.getRegistrationIds().isEmpty()) {
                log.warn("没有registrationId，无法发送");
                return;
            }
            jiGuangPushService.push(bean.getPushBean(), bean.getRegistrationIds().toArray(new String[0]));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
