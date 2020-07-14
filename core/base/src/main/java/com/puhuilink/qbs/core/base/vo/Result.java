package com.puhuilink.qbs.core.base.vo;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String code;

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

    public Result(String errorCode) {
        this.setSuccess(false);
        this.setCode(errorCode);
    }

    public static <T> Result<T> ok(String msg) {
        if (StringUtils.isEmpty(msg)) {
            msg = "ok";
        }
        return new Result<T>().setMessage(msg);
    }

    public static <T> Result<T> ok() {
        return new Result<T>().setMessage("ok");
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(ResultCode.INTERNAL_SERVER_ERROR.getCode()).setMessage(msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        return new Result<T>(code).setMessage(msg);
    }

    public Result<T> data(T t) {
        this.result = t;
        this.code = ResultCode.SUCCESS.getCode();
        return this;
    }

    public Result<T> setMessage(String msg) {
        this.message = msg;
        return this;
    }
}
