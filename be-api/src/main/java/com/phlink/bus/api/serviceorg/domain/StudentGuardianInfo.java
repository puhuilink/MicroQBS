package com.phlink.bus.api.serviceorg.domain;

import com.phlink.bus.api.route.domain.enums.StopAttendanceStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class StudentGuardianInfo implements Serializable {
    private Long studentId;
    private String studentName;
    private Long mainGuardianId;
    private String mainGuardianName;
    private String mainGuardianMobile;
    private String mainGuardianRelation;
    private Long leaveGuardianId;
    private String leaveGuardianName;
    private String leaveGuardianMobile;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;
    private String schoolName;
    private String avatar;
    private String sex;
    private LocalDate birthday;
    private StopAttendanceStateEnum stopAttendanceState;

    public StopAttendanceStateEnum getStopAttendanceState() {
        if(this.stopAttendanceState == null) {
            return StopAttendanceStateEnum.WAIT;
        }
        return this.stopAttendanceState;
    }

}
