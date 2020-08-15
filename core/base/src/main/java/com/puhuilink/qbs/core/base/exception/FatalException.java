package com.puhuilink.qbs.core.base.exception;

public class FatalException extends QbsException {
    private ExceptionLevel level = ExceptionLevel.FATAL;

    public FatalException(int errCode, String desc) {
        super(errCode, desc);
    }

    public FatalException(int errCode, String desc, String ip, int port, String serviceKind) {
        super(errCode, desc, ip, port, serviceKind);
    }

    @Override
    public ExceptionLevel getLevel() {
        return this.level;
    }
}
