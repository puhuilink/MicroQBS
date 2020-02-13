package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.bus.domain.DvrLocation;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AlarmStopLateEvent extends ApplicationEvent {

    private DvrLocation location;

    public AlarmStopLateEvent(Object source, DvrLocation location) {
        super(source);
        this.location = location;
    }
}
