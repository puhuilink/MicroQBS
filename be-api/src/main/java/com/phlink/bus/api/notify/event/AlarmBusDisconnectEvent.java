package com.phlink.bus.api.notify.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AlarmBusDisconnectEvent extends ApplicationEvent {

    private String busCode;

    public AlarmBusDisconnectEvent(Object source, String busCode) {
        super(source);
        this.busCode = busCode;
    }
}
