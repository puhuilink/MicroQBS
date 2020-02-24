package com.phlink.core.web.common.response;

public interface BaseResultInfo {
    /** 错误码*/
    String getResultCode();

    /** 错误描述*/
    String getResultMsg();
}
