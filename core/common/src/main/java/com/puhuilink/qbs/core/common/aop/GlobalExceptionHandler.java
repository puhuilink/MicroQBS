/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:43
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/GlobalExceptionHandler.java
 */
package com.puhuilink.qbs.core.common.aop;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.ErrorException;
import com.puhuilink.qbs.core.base.exception.FatalException;
import com.puhuilink.qbs.core.base.exception.LimitAccessException;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(Exception e) {
        log.error("系统内部异常，未知异常信息：", e);
        return Result.error("系统内部异常");
    }


    @ExceptionHandler(value = WarnException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleWarnException(WarnException e) {
        log.warn("WARN：{}", e.toString());
        return Result.error(e.getErrCode(), e.getDesc()).data(e.toMap());
    }

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleErrorException(ErrorException e) {
        log.error("ERROR：{}", e.toString());
        return Result.error(e.getErrCode(), e.getDesc()).data(e.toMap());
    }

    @ExceptionHandler(value = FatalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleFatalException(FatalException e) {
        log.error("FATAL：{}", e.toString());
        return Result.error(e.getErrCode(), e.getDesc()).data(e.toMap());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> validExceptionHandler(BindException e) {
        log.warn("参数错误：", e);
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return Result.error(ResultCode.BODY_NOT_MATCH.getCode(), message.toString());

    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return BusApiResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("参数错误：", e);
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), StringPool.DOT);
            message.append(pathArr[pathArr.length - 1]).append(violation.getMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return Result.error(ResultCode.BODY_NOT_MATCH.getCode(), message.toString());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求错误", e);
        return Result.error(ResultCode.BODY_NOT_MATCH.getCode(), e.getMessage());

    }

    @ExceptionHandler(value = LimitAccessException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Result<Object> handleLimitAccessException(LimitAccessException e) {
        log.error(e.toString());
        return Result.error(e.getErrCode(), e.getDesc()).data(e.toMap());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数错误，{}", e.getMessage());
        // Get all errors
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() + x.getDefaultMessage()).collect(Collectors.toList());
        return Result.error(ResultCode.BODY_NOT_MATCH.getCode(), String.join(";", errors));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleHttpMessageNotReadableException(MethodArgumentNotValidException e) {
        log.error("参数格式错误，{}", e.getMessage());
        return Result.error(ResultCode.BODY_NOT_MATCH.getCode(), "参数格式错误");
    }
}
