package com.phlink.bus.api.notify.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AddOtherGuardianEvent extends ApplicationEvent {

    private String realname;
    private String studentName;
    private String mobile;

    public AddOtherGuardianEvent(Object source, String realname, String mobile, String studentName) {
        super(source);
        this.realname = realname;
        this.mobile = mobile;
        this.studentName = studentName;
    }
}
