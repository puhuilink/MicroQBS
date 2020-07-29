package com.puhuilink.qbs.core.base.exception;

public class QbsRetryException extends QbsException {
    QbsRetryException(int errCode, String desc, String ip, int port, String serviceKind) {
        super(errCode, desc, ip, port, serviceKind);
    }

    public QbsRetryException(int errCode, String desc) {
        super(errCode, desc);
    }

    @Override
    ExceptionLevel getLevel() {
        return ExceptionLevel.RETRY;
    }
}