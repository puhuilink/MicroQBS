package com.phlink.biz.core.message.manager;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

/**
 * 处理会话事件
 */
@Slf4j
@Setter
public final class SocketSessionManager {

    private RedissonClient redissonClient;

    public void onconnect(SocketIOClient client) {
        log.info("[{}]connect......", client.getSessionId());
    }

    public void onDisconnect(SocketIOClient     client) {
        log.info("[{}]disconnect......", client.getSessionId());
    }

    public void onPing(SocketIOClient client) {
        log.info("[{}]ping......", client.getSessionId());

    }

    public void handshake(HandshakeData data) {
        log.info("[{}] HandshakeData......", data.getAddress());
    }
}