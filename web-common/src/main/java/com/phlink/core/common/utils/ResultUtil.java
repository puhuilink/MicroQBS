package com.phlink.core.common.utils;

import com.phlink.core.common.enums.CommonResultInfo;
import com.phlink.core.common.vo.Result;

public class ResultUtil<T> {

    private Result<T> result;

    public ResultUtil(){
        result=new Result<>();
        result.setSuccess(true);
        result.setMessage("success");
        result.setCode(CommonResultInfo.SUCCESS.getResultCode());
    }

    public Result<T> setData(T t){
        this.result.setResult(t);
        this.result.setCode(CommonResultInfo.SUCCESS.getResultCode());
        return this.result;
    }

    public Result<T> setSuccessMsg(String msg){
        this.result.setSuccess(true);
        this.result.setMessage(msg);
        this.result.setCode(CommonResultInfo.SUCCESS.getResultCode());
        this.result.setResult(null);
        return this.result;
    }

    public Result<T> setData(T t, String msg){
        this.result.setResult(t);
        this.result.setCode(CommonResultInfo.SUCCESS.getResultCode());
        this.result.setMessage(msg);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(CommonResultInfo.FAIL.getResultCode());
        return this.result;
    }

    public Result<T> setErrorMsg(String code, String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(code);
        return this.result;
    }

    public Result<T> setErrorMsg(CommonResultInfo codeEnum){
        this.result.setSuccess(false);
        this.result.setMessage(codeEnum.getResultMsg());
        this.result.setCode(codeEnum.getResultCode());
        return this.result;
    }

    public static <T> Result<T> data(T t){
        return new ResultUtil<T>().setData(t);
    }

    public static <T> Result<T> data(T t, String msg){
        return new ResultUtil<T>().setData(t, msg);
    }

    public static <T> Result<T> success(String msg){
        return new ResultUtil<T>().setSuccessMsg(msg);
    }

    public static <T> Result<T> error(String msg){
        return new ResultUtil<T>().setErrorMsg(msg);
    }

    public static <T> Result<T> error(String code, String msg){
        return new ResultUtil<T>().setErrorMsg(code, msg);
    }
}