package com.phlink.biz.core.message.constant;

public enum PubSubEventType {
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