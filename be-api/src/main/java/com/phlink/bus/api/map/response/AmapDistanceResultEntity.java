package com.phlink.bus.api.map.response;

import lombok.Data;

import java.util.List;

@Data
public class AmapDistanceResultEntity {
    private String status;
    private String info;
    private List<AmapDistanceResult> results;

    public boolean requestSuccess() {
        return "1".equals(status);
    }

    @Data
    public static class AmapDistanceResult {
        private Integer origin_id;
        private Integer dest_id;
        private Integer distance;
        private Integer duration;
        private String info;
        private String code;
    }
}
