package com.phlink.bus.message.constant;

public enum SocketEventType {
    // 告警产生
    ALARM,
    // 告警清除
    ALARM_CLEAR,
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}