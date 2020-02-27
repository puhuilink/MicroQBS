package com.phlink.core.common.utils;

import com.google.gson.Gson;
import com.phlink.core.common.enums.CommonResultInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResponseUtil {

    /**
     * 使用response输出JSON
     *
     * @param response
     * @param resultMap
     */
    public static void out(HttpServletResponse response, Map<String, Object> resultMap) {

        ServletOutputStream out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out = response.getOutputStream();
            out.write(new Gson().toJson(resultMap).getBytes());
        } catch (Exception e) {
            log.error(e + "输出JSON出错");
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Object> resultMap(boolean flag, CommonResultInfo resultInfo, String msg) {

        return resultMap(flag, resultInfo.getResultCode(), msg, null);
    }

    public static Map<String, Object> resultMap(boolean flag, CommonResultInfo resultInfo) {

        return resultMap(flag, resultInfo.getResultCode(), resultInfo.getResultMsg(), null);
    }

    public static Map<String, Object> resultMap(boolean flag, String code, String msg, Object data) {

        Map<String, Object> resultMap = new HashMap<String, Object>(16);
        resultMap.put("success", flag);
        resultMap.put("message", msg);
        resultMap.put("code", code);
        resultMap.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            resultMap.put("result", data);
        }
        return resultMap;
    }
}