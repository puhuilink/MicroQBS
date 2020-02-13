package com.phlink.bus.api.notify.event;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@ToString
@Data
public class FenceDeleteEvent extends ApplicationEvent {

    private List<Long> fenceIds;

    public FenceDeleteEvent(Object source, List<Long> fenceIds) {
        super(source);
        this.fenceIds = fenceIds;
    }
}
