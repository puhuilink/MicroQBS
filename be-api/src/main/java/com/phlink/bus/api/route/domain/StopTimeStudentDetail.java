package com.phlink.bus.api.route.domain;

import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
public class StopTimeStudentDetail implements Serializable {
    private Long stopTimeId;
    private Long stopId;
    private String stopName;
    private LocalTime arrivalTime;
    private Integer stopSequence;
    private BigDecimal stopLon;
    private BigDecimal stopLat;
    private List<StudentGuardianInfo> students;
    private Integer allNum;
    private Integer upNum;
    private Integer downNum;
    private Integer leaveNum;
    private Boolean schoolStop;
}
