package com.phlink.biz.core.message.manager;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.phlink.biz.core.message.dispatcher.MessageHandler;
import com.phlink.biz.core.message.msg.Command;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

/**
 * 处理业务上定义的消息事件
 */
@Slf4j
@Setter
public final class BizEventHandler implements MessageHandler {

    private RedissonClient redissonClient;

    @Override
    public void handle(SocketIOClient client, Command command, final AckRequest ackRequest) {
        log.info("Biz event handle {}", command.toString());
    }
}