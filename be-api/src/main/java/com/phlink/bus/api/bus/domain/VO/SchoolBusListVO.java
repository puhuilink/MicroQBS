package com.phlink.bus.api.bus.domain.VO;

import com.alibaba.fastjson.JSONArray;
import com.phlink.bus.api.bus.domain.Bus;
import lombok.Data;

import java.util.List;

@Data
public class SchoolBusListVO {
    private Long schoolId;
    private String schoolName;
    private Integer onlineCount;
    private Integer offlineCount;

    private JSONArray busList;
}
