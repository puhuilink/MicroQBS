package com.puhuilink.qbs.core.base.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class QbsException extends Exception {

    static String applicationName;
    static String hostname;
    static int hostPort;
    /**
     * Errcode error code(errcode 错误码)
     * Desc error description(desc 错误描述)
     * Ip abnormal server ip(ip 发生异常的服务器ip)
     * Port An abnormal process port(port 发生异常的进程端口)
     * serviceKind microservice type with exception(serviceKind 发生异常的微服务类型)
     */
    private int errCode;
    private String desc;
    private String ip;
    private int port;
    private String serviceKind;

    public QbsException(int errCode, String desc) {
        this(errCode, desc, hostname, hostPort, applicationName);
    }

    public QbsException(int errCode, String desc, String ip, int port, String serviceKind) {
        super("errCode: " + errCode + " ,desc: " + desc + " ,ip: " + ip +
                " ,port: " + port + " ,serviceKind: " + serviceKind);
        this.errCode = errCode;
        this.desc = desc;
        this.ip = ip;
        this.port = port;
        this.serviceKind = serviceKind;
    }

    public static void setApplicationName(String applicationName) {
        QbsException.applicationName = applicationName;
    }

    public static void setHostname(String hostname) {
        QbsException.hostname = hostname;
    }

    public static void setHostPort(int hostPort) {
        QbsException.hostPort = hostPort;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("level", getLevel().getLevel());
        retMap.put("errCode", getErrCode());
        retMap.put("desc", getDesc());
        retMap.put("ip", getIp());
        retMap.put("port", getPort());
        retMap.put("serviceKind", getServiceKind());
        return retMap;
    }

    abstract ExceptionLevel getLevel();

    @Override
    public String toString() {
        return "QBSException{" +
                "errCode=" + errCode +
                ", desc='" + desc + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", serviceKind='" + serviceKind + '\'' +
                '}';
    }
}
