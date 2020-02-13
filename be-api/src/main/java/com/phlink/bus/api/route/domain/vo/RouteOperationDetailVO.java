package com.phlink.bus.api.route.domain.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.system.domain.User;
import lombok.Data;

@Data
public class RouteOperationDetailVO {

    private Long id;
    private JSONObject route;
    private JSONObject bindDriver;
    private JSONObject bindTeacher;
    private JSONObject bindBus;
    private JSONArray stopList;

}
