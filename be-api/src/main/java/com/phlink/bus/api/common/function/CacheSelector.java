package com.phlink.bus.api.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
