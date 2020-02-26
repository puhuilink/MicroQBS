package com.phlink.core.common.exception;

import com.phlink.core.common.enums.CommonResultInfo;

/**
 * 限流异常
 */
public class LoginFailLimitException extends BizException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LoginFailLimitException(String msg) {
        super(CommonResultInfo.LOGIN_FAIL_MANY_TIMES, msg);
    }
}