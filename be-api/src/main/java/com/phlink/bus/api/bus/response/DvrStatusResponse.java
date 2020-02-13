package com.phlink.bus.api.bus.response;

import lombok.Data;

@Data
public class DvrStatusResponse {

    private String devNo;
    private Long lon;
    private Long lat;
    private Integer direction;
    private Float altitude;
    private Float mileage;
    private Float oil;
    private Float speed;
    private Float odbspeed;
    private Integer satellites;
    private Long gpstime;
    private Integer status1;
    private Long status2;
    private Integer status3;
    private Long status4;
    private Integer online;
    private Float glon;
    private Float glat;
    private Float blon;
    private Float blat;

}
