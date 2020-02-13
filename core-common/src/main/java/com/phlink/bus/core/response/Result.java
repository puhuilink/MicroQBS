package com.phlink.bus.core.response;

import lombok.Data;

/**
 * Created by wen on 2016/11/19.
 */
@Data
public class Result<T> {
    private static final String serverErrorMsg = "服务器错误";
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误码
     */
    private Integer code = 200;
    /**
     * 成功 / 失败标识
     */
    private boolean success = true;
    /**
     * 失败信息：用于前端 /api 调用者调试接口
     */
    private String message = "";

    public static <T> Result<T> success(T result) {
        Result<T> response = new Result<>();
        response.data = result;
        return response;
    }

    public static <T> Result<T> success() {
        return new Result<>();
    }

    public static Result error() {
        Result result = new Result<>();
        result.code = 500;
        result.message = serverErrorMsg;
        result.success = false;
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Result<T> error(Integer code, T result) {
        Result<T> response = new Result<>();
        response.success = false;
        response.code = code;
        response.data = result;
        return response;
    }

    public static <T> Result<T> error(Integer code) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = code;
        return result;
    }

}
