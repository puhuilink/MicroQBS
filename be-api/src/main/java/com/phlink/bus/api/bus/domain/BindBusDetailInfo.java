package com.phlink.bus.api.bus.domain;

import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 车辆信息
 *
 * @author wen
 */
@Data
public class BindBusDetailInfo implements Serializable {
    private Long id;
    private String numberPlate;
    private String busCode;
    private String model;
    private String brand;
    private String chassis;
    private String engineModel;
    private String power;
    private String emission;
    private String length;
    private String width;
    private String height;
    private String seat;
    private String fuelTank;
    private Long deptId;
    private String entityName;
    private Long tid;
    private LocalDateTime createTime;
    private Long createBy;
    private LocalDateTime modifyTime;
    private Long modifyBy;
    private LocalDate registerTime;
    private LocalDate checkTime;
    private LocalDate insureTime;
    private LocalDate maintainTime;
    private String dvrCode;
    private Integer channelNumber;
    private Long dvrServerId;
    private Integer online;
    private Long routeId;
    private Long schoolId;
    private String routeName;
    private RouteTypeEnum routeType;
    private Long routeOperationId;
    private Long bindBusTeacherId;
    private String busTeacherName;
    private String busTeacherMobile;
    private Long bindDriverId;
    private String driverName;
    private String driverMobile;

}
