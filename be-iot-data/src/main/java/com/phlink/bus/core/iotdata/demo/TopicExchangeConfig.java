package com.phlink.bus.core.iotdata.demo;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    @Bean
    public Queue myQueue() {
//        Queue queue = new Queue("mq.pitch.concrete");
        Queue queue = new Queue("mq.iot.positionInfo");
        return queue;
    }

    @Bean
    public TopicExchange topicExchange(){
        TopicExchange topicExchange=new TopicExchange("mq.iotExChange");
//        TopicExchange topicExchange=new TopicExchange("mq.pitchExChange2");
        return topicExchange;
    }

    @Bean
    public Binding binding() {
        Binding binding = BindingBuilder.bind(myQueue()).to(topicExchange()).with("mq.positionInfo.send");
        return binding;
    }
}
