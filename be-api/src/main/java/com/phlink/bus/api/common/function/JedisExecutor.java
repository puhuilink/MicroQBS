package com.phlink.bus.api.common.function;

import com.phlink.bus.api.common.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
