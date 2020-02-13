package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author wen
 * @create 2019-09-27 10:12
 */
@ToString
@Data
public class TripStartEvent extends ApplicationEvent {
    private TripState tripState;
    private TripLog tripLog;

    public TripStartEvent(Object source, TripState tripState, TripLog tripLog) {
        super(source);
        this.tripState = tripState;
        this.tripLog = tripLog;
    }
}
