package com.phlink.bus.message.dispatcher;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.phlink.bus.message.msg.Command;

public interface MessageHandler {
    void handle(SocketIOClient client, Command command, final AckRequest ackRequest);
}