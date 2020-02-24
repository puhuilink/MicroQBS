package com.phlink.core.web.common.exception;

import com.phlink.core.web.common.response.CommonResultEnum;

/**
 * 限流异常
 */
public class LimitAccessException extends BizException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException() {
        super(CommonResultEnum.TOO_MANY_REQUESTS);
    }
}