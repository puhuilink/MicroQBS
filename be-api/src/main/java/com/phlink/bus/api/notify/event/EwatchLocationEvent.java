package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.device.domain.EwatchLocation;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class EwatchLocationEvent extends ApplicationEvent {

    private EwatchLocation ewatchLocation;

    public EwatchLocationEvent(Object source, EwatchLocation ewatchLocation) {
        super(source);
        this.ewatchLocation = ewatchLocation;
    }
}
