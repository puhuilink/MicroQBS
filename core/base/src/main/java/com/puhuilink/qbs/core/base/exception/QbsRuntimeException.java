package com.puhuilink.qbs.core.base.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.puhuilink.qbs.core.base.exception.QbsException.*;

@Data
public abstract class QbsRuntimeException extends RuntimeException {

    private int errCode;
    private String desc;
    private String ip;
    private int port;
    private String serviceKind;

    public QbsRuntimeException(int errCode, String desc) {
        this(errCode, desc, hostname, hostPort, applicationName);
    }

    public QbsRuntimeException(int errCode, String desc, String ip, int port, String serviceKind) {
        super("errCode: " + errCode + " ,desc: " + desc + " ,ip: " + ip +
                " ,port: " + port + " ,serviceKind: " + serviceKind);
        this.errCode = errCode;
        this.desc = desc;
        this.ip = ip;
        this.port = port;
        this.serviceKind = serviceKind;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("errCode", getErrCode());
        retMap.put("desc", getDesc());
        retMap.put("ip", getIp());
        retMap.put("port", getPort());
        retMap.put("level", getLevel().getLevel());
        retMap.put("serviceKind", getServiceKind());
        return retMap;
    }

    public abstract ExceptionLevel getLevel();

    @Override
    public String toString() {
        return "QbsException{" +
                "errCode=" + errCode +
                ", desc='" + desc + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", serviceKind='" + serviceKind + '\'' +
                '}';
    }
}
