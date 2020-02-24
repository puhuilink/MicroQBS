package com.phlink.core.web.common.entity;

import com.alibaba.fastjson.JSONObject;
import com.phlink.core.web.common.response.BaseResultInfo;
import com.phlink.core.web.common.response.CommonResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBody {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object data;

    public ResultBody(BaseResultInfo errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResultBody success() {
        return success(null);
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static ResultBody success(Object data) {
        ResultBody rb = new ResultBody();
        rb.setCode(CommonResultEnum.SUCCESS.getResultCode());
        rb.setMessage(CommonResultEnum.SUCCESS.getResultMsg());
        rb.setData(data);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(BaseResultInfo errorInfo) {
        ResultBody rb = new ResultBody();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setData(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String code, String message) {
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String message) {
        ResultBody rb = new ResultBody();
        rb.setCode(CommonResultEnum.FAIL.getResultCode());
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
