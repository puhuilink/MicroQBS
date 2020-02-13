package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.im.domain.ImGroups;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class ImGroupDeleteEvent extends ApplicationEvent {
    private String[] ids;

    public ImGroupDeleteEvent(Object source, String[] ids) {
        super(source);
        this.ids = ids;
    }

}
