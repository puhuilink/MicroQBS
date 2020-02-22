package com.phlink.biz.core.message.dispatcher;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.phlink.biz.core.message.msg.Command;

public interface MessageHandler {
    void handle(SocketIOClient client, Command command, final AckRequest ackRequest);
}