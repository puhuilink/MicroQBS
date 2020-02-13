package com.phlink.bus.api.common.handler;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.LimitAccessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.redisson.client.RedisTimeoutException;
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
    public BusApiResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息：", e);
        return new BusApiResponse().message("系统内部异常");
    }

    @ExceptionHandler(value = BusApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusApiResponse handleParamsInvalidException(BusApiException e) {
        log.warn("系统错误：{}", e.getMessage());
        return new BusApiResponse().message(e.getMessage());
    }

    @ExceptionHandler(value = RedisTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusApiResponse handleRedisTimeoutException(RedisTimeoutException e) {
        log.error("redis连接超时：{}", e.getMessage());
        return new BusApiResponse().message("服务连接超时");
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return BusApiResponse
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusApiResponse validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new BusApiResponse().message(message.toString());

    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return BusApiResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusApiResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), StringPool.DOT);
            message.append(pathArr[pathArr.length - 1]).append(violation.getMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new BusApiResponse().message(message.toString());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusApiResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new BusApiResponse().message(e.getMessage());

    }

    @ExceptionHandler(value = LimitAccessException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public BusApiResponse handleLimitAccessException(LimitAccessException e) {
        log.warn(e.getMessage());
        return new BusApiResponse().message(e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BusApiResponse handleUnauthorizedException(Exception e) {
        log.error("权限不足，{}", e.getMessage());
        return new BusApiResponse().message("权限不足");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("参数错误，{}", e.getMessage());
        //Get all errors
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + x.getDefaultMessage())
                .collect(Collectors.toList());
        return new BusApiResponse().message(String.join(";", errors));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusApiResponse handleHttpMessageNotReadableException(MethodArgumentNotValidException e) {
        log.warn("参数格式错误，{}", e.getMessage());
        return new BusApiResponse().message("参数格式错误");
    }
}
