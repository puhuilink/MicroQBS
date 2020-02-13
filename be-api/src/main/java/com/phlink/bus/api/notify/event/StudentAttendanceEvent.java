package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.route.domain.StopAttendance;
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
public class StudentAttendanceEvent extends ApplicationEvent {
    private StopAttendance stopAttendance;
    private TripState tripState;

    public StudentAttendanceEvent(Object source, TripState tripState, StopAttendance stopAttendance) {
        super(source);
        this.stopAttendance = stopAttendance;
        this.tripState = tripState;
    }
}
