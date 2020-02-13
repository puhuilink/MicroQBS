package com.phlink.bus.api.bus.domain.VO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class SchoolRouteBusListVO {
    private Long schoolId;
    private String schoolName;

    private JSON routeList;
}
