package com.phlink.bus.api.common.exception;

/**
 * 系统内部异常
 */
public class BusApiException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public BusApiException() {
        super();
    }

    public BusApiException(String message) {
        super(message);
    }

    public BusApiException(String message, Throwable e) {
        super(message, e);
    }
}
