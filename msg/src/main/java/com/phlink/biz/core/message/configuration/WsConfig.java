package com.phlink.biz.core.message.configuration;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
//@Configuration
public class WsConfig {
    @Autowired
    private RedissonClient redissonClient;
    @Value("${websocket.server.host}")
    private String host;
    @Value("${websocket.server.port}")
    private Integer port;

//    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
//        config.setTransports(Transport.WEBSOCKET);
        SocketConfig socketConfig = config.getSocketConfig();
        socketConfig.setReuseAddress(true);
        RedissonStoreFactory redisStoreFactory = new RedissonStoreFactory(redissonClient);
        config.setStoreFactory(redisStoreFactory);
        SocketIOServer socketIOServer = new SocketIOServer(config);
        socketIOServer.addConnectListener(new ConnectListener() {

            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                log.info("connect...");
            }
        });
        socketIOServer.addDisconnectListener(new DisconnectListener() {

            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                log.info("disconnect...");
            }
        });
        return socketIOServer;
    }

}
