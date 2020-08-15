package com.puhuilink.qbs.core.base.vo;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.QbsException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;

    public Result() {
        this.setSuccess(true);
        this.setMessage("ok");
        this.setCode(ResultCode.SUCCESS.getCode());
    }

    public Result(Integer errorCode) {
        this.setSuccess(false);
        this.setCode(errorCode);
    }

    public static <T> Result<T> ok(String msg) {
        if (StringUtils.isEmpty(msg)) {
            msg = "ok";
        }
        return new Result<T>().msg(msg);
    }

    public static <T> Result<T> ok() {
        return new Result<T>().msg("ok");
    }

    public static <T> Result<T> error() {
        return new Result<T>(ResultCode.INTERNAL_SERVER_ERROR.getCode());
    }

    public static <T> Result<T> error(Integer code) {
        return new Result<T>(code);
    }

    public static Result<String> error(QbsException e) {
        return new Result<String>(e.getErrCode()).msg(e.getMessage()).data(e.toString());
    }

    public Result<T> data(T t) {
        this.result = t;
        return this;
    }

    public Result<T> msg(String msg) {
        this.message = msg;
        return this;
    }
}
