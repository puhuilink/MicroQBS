package com.puhuilink.qbs.core.base.exception;

import java.util.HashMap;
import java.util.Map;

import static com.puhuilink.qbs.core.base.exception.ExceptionLevel.*;

public final class ExceptionManager {
    private static final String LEVEL = "level";
    private static final String ERRCODE = "errCode";
    private static final String DESC = "desc";
    private static final String IP = "ip";
    private static final String PORT = "port";
    private static final String SERVICEKIND = "serviceKind";

    public static Exception generateException(Map<String, Object> map) {
        Exception retException = null;
        if (null == map || map.get(LEVEL) == null) {
            return new ErrorException(10000, "The map cannot be parsed normally, the map is empty or the LEVEL value is missing:(map不能被正常的解析，map为空或者缺少LEVEL值: )" + map);
        }
        int level = Integer.parseInt(map.get(LEVEL).toString());
        int errCode = Integer.parseInt(map.get(ERRCODE).toString());
        String desc = (String) map.get(DESC);
        String ip = (String) map.get(IP);
        int port = Integer.parseInt(map.get(PORT).toString());
        String serviceKind = (String) map.get(SERVICEKIND);
        if (WARN.getLevel() == level) {
            retException = new WarnException(errCode, desc, ip, port, serviceKind);
        } else if (ERROR.getLevel() == level) {
            retException = new ErrorException(errCode, desc, ip, port, serviceKind);
        } else if (FATAL.getLevel() == level) {
            retException = new FatalException(errCode, desc, ip, port, serviceKind);
        } else if (RETRY.getLevel() == level) {
            retException = new QbsRetryException(errCode, desc, ip, port, serviceKind);
        }
        return retException != null ? retException : new ErrorException(10000, "Exception Map that cannot be parsed:(不能解析的异常Map：)" + map);
    }

    public static Map<String, Object> unknownException(String errorMsg) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("level", ERROR.getLevel());
        retMap.put("errCode", 0);
        retMap.put("desc", errorMsg);
        retMap.put("ip", QbsException.hostname);
        retMap.put("port", QbsException.hostPort);
        retMap.put("serviceKind", QbsException.applicationName);
        return retMap;
    }
}