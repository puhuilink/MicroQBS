package com.phlink.bus.api.large.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/13$ 10:14$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/13$ 10:14$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class LineList {

    private Long tripId;

    private Long userId;

    private Long routeId;

    private String routeName;

    private Long directionId;

    private String teacherName;

    private Long busId;

    private String driverName;

    private String standStop;

    private String endStop;

    private Long ydrs;

    private Long qjrs;

    private Long sdrs;

    private Date time;

    private String teachermobile;

    private String drivermobile;

    private String stopName;

    private Long trajectoryId;

    private String numberPlate;

    private String teacheravatar;

    private String driveravatar;


}
