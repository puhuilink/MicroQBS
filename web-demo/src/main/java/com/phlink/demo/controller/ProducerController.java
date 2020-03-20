package com.phlink.demo.controller;

import com.google.gson.Gson;
import com.phlink.demo.entity.MsgLog;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProducerController {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public String sendMsg () {
        MsgLog msgLog = new MsgLog(1,"消息生成", System.currentTimeMillis()) ;
        String msg = new Gson().toJson(msgLog);
        // 这里Topic如果不存在，会自动创建
        kafkaTemplate.send("demo-topic", msg);
        return msg ;
    }

}
