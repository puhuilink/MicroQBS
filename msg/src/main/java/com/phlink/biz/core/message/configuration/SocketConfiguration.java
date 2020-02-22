package com.phlink.biz.core.message.configuration;

import com.phlink.biz.core.message.constant.SocketEventType;
import com.phlink.biz.core.message.dispatcher.EventDispatcher;
import com.phlink.biz.core.message.manager.BizEventHandler;
import com.phlink.biz.core.message.manager.SocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SocketConfiguration {

    @Value("${websocket.server.host}")
    private String host;
    @Value("${websocket.server.port}")
    private Integer port;
    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public EventDispatcher eventDispatcher() {

        BizEventHandler bizEventHandler = new BizEventHandler();
        bizEventHandler.setRedissonClient(redissonClient);

        SocketSessionManager sessionManager = new SocketSessionManager();
        sessionManager.setRedissonClient(redissonClient);

        EventDispatcher eventDispatcher = new EventDispatcher();
        eventDispatcher.setSessionManager(sessionManager);

        for (SocketEventType eventType : SocketEventType.values()) {
            eventDispatcher.register(eventType, bizEventHandler);
        }
        return eventDispatcher;
    }

    @Bean(destroyMethod = "stop")
    public SocketServer socketServer(@Autowired EventDispatcher eventDispatcher) {
        SocketServer socketServer = new SocketServer(eventDispatcher, redissonClient);
        socketServer.init(host, port);
        socketServer.start();
        return socketServer;
    }
}