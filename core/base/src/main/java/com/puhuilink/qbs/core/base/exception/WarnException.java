package com.puhuilink.qbs.core.base.exception;

public class WarnException extends QbsRuntimeException {
    private ExceptionLevel level = ExceptionLevel.WARN;

    public WarnException(int errCode, String desc) {
        super(errCode, desc);
    }

    public WarnException(int errCode, String desc, String ip, int port, String serviceKind) {
        super(errCode, desc, ip, port, serviceKind);
    }

    @Override
    public ExceptionLevel getLevel() {
        return this.level;
    }
}
