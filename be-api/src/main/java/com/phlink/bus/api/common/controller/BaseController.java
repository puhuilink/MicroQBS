package com.phlink.bus.api.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<>();
        if(pageInfo == null) {
            rspData.put("rows", Collections.EMPTY_LIST);
            rspData.put("total", 0);
        }else{
            rspData.put("rows", pageInfo.getRecords());
            rspData.put("total", (int)pageInfo.getTotal());
        }
        return rspData;
    }
    
    protected Map<String, Object> getDataTable(List<?> list) {
        Map<String, Object> rspData = new HashMap<>();
        if(list == null || list.isEmpty()) {
            rspData.put("rows", Collections.EMPTY_LIST);
            rspData.put("total", 0);
        }else{
            rspData.put("rows", list);
            rspData.put("total", list.size());
        }
        return rspData;
    }
    
    protected Map<String, Object> returnSuccess(Object obj) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("code", 200);
        rspData.put("data",obj);
        rspData.put("message","操作成功");
        return rspData;
    }
    protected Map<String, Object> returnFail(Object obj) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("code", 406);
        rspData.put("data",null);
        rspData.put("message",obj);
        return rspData;
    }
}
