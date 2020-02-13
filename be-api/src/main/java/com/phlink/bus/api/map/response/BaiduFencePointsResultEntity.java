package com.phlink.bus.api.map.response;

import lombok.Data;

import java.util.List;

@Data
public class BaiduFencePointsResultEntity {
    private Integer fence_id;
    private String fence_name;
    private String monitored_person;
    private String shape;
    private Double longitude;
    private Double latitude;
    private Double radius;
    private List<BaiduPointResultEntity> vertexes;
    private Double offset;
    private String coord_type;
    private Integer denoise;
    private String district;
    private String create_time;
    private String modify_time;
}
