package com.phlink.bus.api.alarm.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class AlarmStatisticsDay {
    private Integer allCount;
    private Integer processCount;
    private Map<String, JSONObject> data;
}
