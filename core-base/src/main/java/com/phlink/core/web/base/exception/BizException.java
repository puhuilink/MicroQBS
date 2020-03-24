package com.phlink.core.web.base.exception;

import com.phlink.core.web.base.enums.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizException extends RuntimeException {
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException(IResultCode resultCode) {
        super(resultCode.getCode());
        this.errorCode = resultCode.getCode();
        this.errorMsg = resultCode.getMsg();
    }

    public BizException(IResultCode resultCode, String message) {
        super(message);
        this.errorCode = resultCode.getCode();
        this.errorMsg = message;
    }

    public BizException(IResultCode resultCode, Throwable cause) {
        super(resultCode.getCode(), cause);
        this.errorCode = resultCode.getCode();
        this.errorMsg = resultCode.getMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(IResultCode resultCode, String errorMsg, Throwable cause) {
        super(resultCode.getCode(), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
