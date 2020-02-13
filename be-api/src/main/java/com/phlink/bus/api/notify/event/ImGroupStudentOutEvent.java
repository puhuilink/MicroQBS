package com.phlink.bus.api.notify.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Data
public class ImGroupStudentOutEvent extends ApplicationEvent {
    private Long routeOperationId;
    private List<Long> studentIds;

    public ImGroupStudentOutEvent(Object source, Long routeOperationId, List<Long> studentIds) {
        super(source);
        this.routeOperationId = routeOperationId;
        this.studentIds = studentIds;
    }
}
