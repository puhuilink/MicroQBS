package com.phlink.bus.api.alarm.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class AlarmTypeStatistics {
    private String alarmType;
    private JSONObject data;

}
