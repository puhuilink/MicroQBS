package com.phlink.biz.core.message.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.phlink.bus.common.Constants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsService {

    @Autowired
    private CCPRestSDK ccpRestSDK;
    @Autowired
    private RedissonClient redissonClient;

    @RabbitListener(queues = Constants.MQ_QUEUE_SEND_SMS)
    @RabbitHandler
    public void process(Object object, Channel channel, Message message) throws Exception {
        try {
            String msg = new String(message.getBody());
            JSONObject content = JSON.parseObject(msg);
            String type = content.getString("type");
            String mobile = content.getString("mobile");
            if(Constants.MSG_TYPE_CAPTCHA.equals(type)) {
                sendCaptcha(mobile);
            }else if(Constants.MSG_TYPE_ADD_GUARDIAN.equals(type)) {
                sendAddGuardian(mobile);
            }else if(Constants.MSG_TYPE_BIND_GUARDIAN.equals(type)) {
                sendBindGuardian(mobile);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendCaptcha(String mobile) {
        // 获取redis的验证码
        String captcha = null;
        RBucket<String> bucket = redissonClient.getBucket(Constants.CAPTCHA_CACHE_PREFIX + mobile, new StringCodec());
        if(bucket == null) {
            log.error("手机号[{}]的验证码已过期", mobile);
            return;
        }else{
            captcha = bucket.get();
            if(StringUtils.isBlank(captcha)) {
                log.error("手机号[{}]的验证码不存在", mobile);
                return;
            }
        }
        log.info("send sms to {}",mobile);

        SendSms sendSms = new SendSms();
        String templateParams="[\""+captcha+"\"]";
        if(sendSms.Send("newCode", mobile, templateParams)){
            log.info("sendCaptcha {} 短信发送成功", mobile);
        }
        else{
            log.info("sendCaptcha {} 短信发送失败", mobile);
        }
    }

    private void sendAddGuardian(String mobile) {
        // 获取添加监护人绑定的信息
        RMap<String, String> rmap = redissonClient.getMap(Constants.ADD_GUARDIAN_CACHE_PREFIX + mobile);
        if(rmap == null) {
            log.error("手机号[{}]的信息已过期", mobile);
            return;
        }
        log.info("send sms to {}",mobile);
        String realname = rmap.get("realname");
        String studentName = rmap.get("studentName");
        SendSms sendSms = new SendSms();
        String templateParams="[\"" + realname + "\",\"" + studentName + "\",\"" + "010-60444307" + "\"]";
        if(sendSms.Send("bindGuardian", mobile, templateParams)){
            log.info("sendAddGuardian {} 短信发送成功", mobile);
        }
        else{
            log.info("sendAddGuardian {} 短信发送失败", mobile);
        }
    }


    private void sendBindGuardian(String mobile) {
        // 获取绑定监护人绑定的信息
        RMap<String, String> rmap = redissonClient.getMap(Constants.BIND_GUARDIAN_CACHE_PREFIX + mobile);
        if(rmap == null) {
            log.error("手机号[{}]的信息已过期", mobile);
            return;
        }
        log.info("send sms to {}",mobile);
        String realname = rmap.get("realname");
        String studentName = rmap.get("studentName");
        SendSms sendSms = new SendSms();
        String templateParams="[\"" + realname + "\",\"" + studentName + "\",\"" + "010-60444307" + "\"]";
        if(sendSms.Send("addGuardian", mobile, templateParams)){
            log.info("sendBindGuardian {} 短信发送成功", mobile);
        }
        else{
            log.info("sendBindGuardian {} 短信发送失败", mobile);
        }
    }
}
