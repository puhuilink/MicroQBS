package com.puhuilink.qbs.core.base.exception;

public class QbsCommonErrorException extends ErrorException {
    public QbsCommonErrorException(int errCode, String desc) {
        super(errCode, desc);
    }

    public QbsCommonErrorException(int errCode, String desc, String ip, int port, String serviceKind) {
        super(errCode, desc, ip, port, serviceKind);
    }
}
