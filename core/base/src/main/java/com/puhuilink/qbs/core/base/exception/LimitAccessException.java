package com.puhuilink.qbs.core.base.exception;

import com.puhuilink.qbs.core.base.enums.ResultCode;

/**
 * 限流异常
 */
public class LimitAccessException extends ErrorException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException(String errorMsg) {
        super(ResultCode.TOO_MANY_REQUESTS.getCode(), errorMsg);
    }
}
