package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.im.domain.ImGroups;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class ImGroupCreateEvent extends ApplicationEvent {
    private ImGroups imGroups;
    private Long[] memberIds;

    public ImGroupCreateEvent(Object source, ImGroups imGroups, Long[] memberIds) {
        super(source);
        this.imGroups = imGroups;
        this.memberIds = memberIds;
    }

}
