package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.bus.domain.DvrLocation;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AlarmBusMappingEvent extends ApplicationEvent {

    private AlarmRouteRules rules;
    private DvrLocation dvrLocation;

    public AlarmBusMappingEvent(Object source, AlarmRouteRules rules, DvrLocation dvrLocation) {
        super(source);
        this.rules = rules;
        this.dvrLocation = dvrLocation;
    }
}
