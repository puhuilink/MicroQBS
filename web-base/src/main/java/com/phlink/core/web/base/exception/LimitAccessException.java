package com.phlink.core.web.base.exception;

/**
 * 限流异常
 */
public class LimitAccessException extends BizException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException() {
        super(CommonEnum.TOO_MANY_REQUESTS);
    }
}