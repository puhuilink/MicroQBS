package com.phlink.bus.api.notify.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class ImGroupStudentEnterEvent extends ApplicationEvent {
    private Long routeOperationId;
    private List<Long> studentIds;

    public ImGroupStudentEnterEvent(Object source, Long routeOperationId, List<Long> studentIds) {
        super(source);
        this.routeOperationId = routeOperationId;
        this.studentIds = studentIds;
    }

    public Long getRouteOperationId() {
        return routeOperationId;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }
}
