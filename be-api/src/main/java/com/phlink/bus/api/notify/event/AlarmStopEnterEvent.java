package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import org.springframework.context.ApplicationEvent;

public class AlarmStopEnterEvent extends ApplicationEvent {
    private StopTimeStudentDetail stopTime;
    private StopTimeStudentDetail nextStopTime;
    private TripState tripState;

    public AlarmStopEnterEvent(Object source, StopTimeStudentDetail stopTime, StopTimeStudentDetail nextStopTime, TripState tripState) {
        super(source);
        this.stopTime = stopTime;
        this.nextStopTime = nextStopTime;
        this.tripState = tripState;
    }

    public StopTimeStudentDetail getStopTime() {
        return stopTime;
    }

    public StopTimeStudentDetail getNextStopTime() {
        return nextStopTime;
    }

    public TripState getTripState() {
        return tripState;
    }
}
