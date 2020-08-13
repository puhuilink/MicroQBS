package com.puhuilink.qbs.core.limiter.exception;


import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;

/**
 * 限流异常
 */
public class LimitAccessException extends WarnException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException(String desc) {
        super(ResultCode.TOO_MANY_REQUESTS.getCode(), desc);
    }
}
