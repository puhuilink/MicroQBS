package com.phlink.qbs.core.base.utils;

import com.phlink.qbs.core.base.enums.ResultCode;
import com.phlink.qbs.core.base.vo.Result;

public class ResultUtil<T> {

    private Result<T> result;

    public ResultUtil() {
        result = new Result<>();
        result.setSuccess(true);
        result.setMessage("success");
        result.setCode(ResultCode.SUCCESS.getCode());
    }

    public static <T> Result<T> data(T t) {
        return new ResultUtil<T>().setData(t);
    }

    public static <T> Result<T> data(T t, String msg) {
        return new ResultUtil<T>().setData(t, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new ResultUtil<T>().setSuccessMsg(msg);
    }

    public static <T> Result<T> error(String msg) {
        return new ResultUtil<T>().setErrorMsg(msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        return new ResultUtil<T>().setErrorMsg(code, msg);
    }

    public Result<T> setData(T t) {
        this.result.setResult(t);
        this.result.setCode(ResultCode.SUCCESS.getCode());
        return this.result;
    }

    public Result<T> setSuccessMsg(String msg) {
        this.result.setSuccess(true);
        this.result.setMessage(msg);
        this.result.setCode(ResultCode.SUCCESS.getCode());
        this.result.setResult(null);
        return this.result;
    }

    public Result<T> setData(T t, String msg) {
        this.result.setResult(t);
        this.result.setCode(ResultCode.SUCCESS.getCode());
        this.result.setMessage(msg);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg) {
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(ResultCode.FAIL.getCode());
        return this.result;
    }

    public Result<T> setErrorMsg(String code, String msg) {
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(code);
        return this.result;
    }

    public Result<T> setErrorMsg(ResultCode codeEnum) {
        this.result.setSuccess(false);
        this.result.setMessage(codeEnum.getMsg());
        this.result.setCode(codeEnum.getCode());
        return this.result;
    }
}
