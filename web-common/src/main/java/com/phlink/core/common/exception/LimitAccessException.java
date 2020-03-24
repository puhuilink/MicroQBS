package com.phlink.core.common.exception;

import com.phlink.core.common.enums.ResultCode;

/**
 * 限流异常
 */
public class LimitAccessException extends BizException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException() {
        super(ResultCode.TOO_MANY_REQUESTS);
    }
}