package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.route.domain.vo.StudentAttendanceVO;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Data
public class LeavePermissionChangeEvent extends ApplicationEvent {
    private Long guardianId;
    private Long mainId;
    private Long studentId;
    private Boolean isLeavePermission;

    public LeavePermissionChangeEvent(Object source, Long guardianId, Long mainId, Long studentId, Boolean isLeavePermission) {
        super(source);
        this.guardianId = guardianId;
        this.mainId = mainId;
        this.studentId = studentId;
        this.isLeavePermission = isLeavePermission;
    }

}
