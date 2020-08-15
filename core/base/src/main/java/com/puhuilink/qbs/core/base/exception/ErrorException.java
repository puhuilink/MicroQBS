package com.puhuilink.qbs.core.base.exception;

public class ErrorException extends QbsException {
    private ExceptionLevel level = ExceptionLevel.ERROR;

    public ErrorException(int errCode, String desc) {
        super(errCode, desc);
    }

    public ErrorException(int errCode, String desc, String ip, int port, String serviceKind) {
        super(errCode, desc, ip, port, serviceKind);
    }

    @Override
    public ExceptionLevel getLevel() {
        return this.level;
    }
}
