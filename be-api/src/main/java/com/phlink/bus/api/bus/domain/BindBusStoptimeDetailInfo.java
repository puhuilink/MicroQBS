package com.phlink.bus.api.bus.domain;

import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 车辆信息
 *
 * @author wen
 */
@Data
public class BindBusStoptimeDetailInfo implements Serializable {
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
    private Long routeId;
    private Long schoolId;
    private String routeName;
    private RouteTypeEnum routeType;
    private Long routeOperationId;
    private Long bindBusTeacherId;
    private Long bindDriverId;
    private Long tripId;
    private TripRedirectEnum directionId;
    private Long stopId;
    private String stopName;
    private Integer stopSequence;
    private Long stopTimeId;
    private LocalTime arrivalTime;
    private BigDecimal stopLon;
    private BigDecimal stopLat;

}
