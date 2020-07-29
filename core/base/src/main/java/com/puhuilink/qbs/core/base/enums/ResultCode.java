package com.puhuilink.qbs.core.base.enums;


public enum ResultCode implements IResultCode {
    // 数据操作错误定义
    SUCCESS(0, "成功!"),
    // 系统失败
    AUTHENTICATION(10, "AUTHENTICATION"),
    JWT_TOKEN_EXPIRED(11, "JWT_TOKEN_EXPIRED"),
    CREDENTIALS_EXPIRED(15, "CREDENTIALS_EXPIRED"),
    LOGIN_FAIL_MANY_TIMES(19, "登录失败次数过多!"),
    PERMISSION_DENIED(20, "PERMISSION_DENIED"),
    INVALID_ARGUMENTS(30, "INVALID_ARGUMENTS"),
    BAD_REQUEST_PARAMS(31, "BAD_REQUEST_PARAMS"),
    ITEM_NOT_FOUND(32, "ITEM_NOT_FOUND"),
    TOO_MANY_REQUESTS(33, "TOO_MANY_REQUESTS"),
    TOO_MANY_UPDATES(34, "TOO_MANY_UPDATES"),

    BODY_NOT_MATCH(40,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(41,"请求的数字签名不匹配!"),
    FORBIDDEN(42,"抱歉，您没有访问权限!"),
    INTERNAL_SERVER_ERROR(50, "服务器内部错误!"),
    SERVER_BUSY(53,"服务器正忙，请稍后再试!"),

    // 业务失败
    FAIL(1, "操作失败!"),
    ;

    /** 错误码 */
    private Integer code;

    /** 错误描述 */
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
