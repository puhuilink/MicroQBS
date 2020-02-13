package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.route.domain.TripLog;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author wen
 * @create 2019-09-27 10:12
 */
@ToString
@Data
public class BusStopEnterEvent extends ApplicationEvent {
    private String busCode;
    private Long stopId;

    public BusStopEnterEvent(Object source, String busCode, Long stopId) {
        super(source);
        this.busCode = busCode;
        this.stopId = stopId;
    }
}
