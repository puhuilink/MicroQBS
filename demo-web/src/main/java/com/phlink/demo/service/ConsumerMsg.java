package com.phlink.demo.service;

import com.phlink.demo.entity.MsgLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ConsumerMsg {

    @KafkaListener(topics = "demo-topic")
    public void listenMsg (ConsumerRecord<?,String> record) {
        String value = record.value();
        log.info("ConsumerMsg====>>"+value);
    }

    @Async
    @KafkaListener(topics = "spark-topic")
    public void listenSpark (ConsumerRecord<?,String> record) {
        String value = record.value();
        log.info("ConsumerSpark====>>"+value);
    }
}