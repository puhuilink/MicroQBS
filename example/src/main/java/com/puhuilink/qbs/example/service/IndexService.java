/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 15:02
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 15:02
 */
package com.puhuilink.qbs.example.service;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-13 15:02
 **/
@Slf4j
@Service
public class IndexService {

    @Async
    @Retryable(value = {WarnException.class}, maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    public void getByNameRetry(String name) {
        log.info("getByNameRetry {}", name);
        throw new WarnException(ResultCode.FAIL.getCode(), "发生错误，重试。。。");
    }
}
