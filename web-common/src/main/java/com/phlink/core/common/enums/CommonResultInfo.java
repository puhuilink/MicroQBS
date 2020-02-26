package com.phlink.core.common.enums;

public enum CommonResultInfo implements BaseResultInfo {
    // 数据操作错误定义
    SUCCESS("200", "成功!"),
    // 系统失败
    BODY_NOT_MATCH("400","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配!"),
    FORBIDDEN("403","抱歉，您没有访问权限!"),
    NOT_FOUND("404", "未找到该资源!"),
    TOO_MANY_REQUESTS("429", "接口访问超出频率限制!"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    SERVER_BUSY("503","服务器正忙，请稍后再试!"),
    // 业务失败
    FAIL("-100", "操作失败!"),
    LOGIN_FAIL_MANY_TIMES("-101", "登录失败次数过多!"),
    ;

    /** 错误码 */
    private String resultCode;

    /** 错误描述 */
    private String resultMsg;

    CommonResultInfo(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }

}