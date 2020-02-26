package com.phlink.core.common.exception;

import com.phlink.core.common.enums.BaseResultInfo;
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

    public BizException(BaseResultInfo errorInfo) {
        super(errorInfo.getResultCode());
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public BizException(BaseResultInfo errorInfo, String message) {
        super(message);
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = message;
    }

    public BizException(BaseResultInfo errorInfo, Throwable cause) {
        super(errorInfo.getResultCode(), cause);
        this.errorCode = errorInfo.getResultCode();
        this.errorMsg = errorInfo.getResultMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(BaseResultInfo errorInfo, String errorMsg, Throwable cause) {
        super(errorInfo.getResultCode(), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
