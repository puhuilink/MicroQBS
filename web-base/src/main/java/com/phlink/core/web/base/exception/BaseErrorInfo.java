package com.phlink.core.web.base.exception;

public interface BaseErrorInfo {
    /** 错误码*/
    String getResultCode();

    /** 错误描述*/
    String getResultMsg();
}
