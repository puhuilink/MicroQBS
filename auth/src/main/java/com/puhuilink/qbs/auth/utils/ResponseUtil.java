/*
 * @Author: sevncz.wen
 * @Date: 2020-03-26 20:56:00
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:13:40
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/utils/ResponseUtil.java
 */
package com.puhuilink.qbs.auth.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

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
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
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

    public static Map<String, Object> resultMap(boolean flag, ResultCode resultCode, String msg) {

        return resultMap(flag, resultCode.getCode(), msg, null);
    }

    public static Map<String, Object> resultMap(boolean flag, ResultCode resultCode) {

        return resultMap(flag, resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static Map<String, Object> resultMap(boolean flag, Integer code, String msg, Object data) {

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
