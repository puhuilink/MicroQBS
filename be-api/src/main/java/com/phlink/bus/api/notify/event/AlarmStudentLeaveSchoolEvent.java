package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.device.domain.EwatchLocation;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AlarmStudentLeaveSchoolEvent extends ApplicationEvent {

    private EwatchLocation location;

    public AlarmStudentLeaveSchoolEvent(Object source, EwatchLocation location) {
        super(source);
        this.location = location;
    }
}
