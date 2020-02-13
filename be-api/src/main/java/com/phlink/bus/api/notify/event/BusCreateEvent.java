package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.bus.domain.Bus;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Data
public class BusCreateEvent extends ApplicationEvent {
    private Bus bus;

    public BusCreateEvent(Object source, Bus bus) {
        super(source);
        this.bus = bus;
    }
}
