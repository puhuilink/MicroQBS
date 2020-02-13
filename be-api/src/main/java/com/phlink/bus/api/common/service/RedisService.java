package com.phlink.bus.api.common.service;

import com.phlink.bus.api.common.domain.RedisInfo;
import com.phlink.bus.api.common.exception.RedisConnectException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

    /**
     * 获取 redis 的详细信息
     *
     * @return List
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     */
    Set<String> getKeys(String pattern) throws RedisConnectException;

    /**
     * get命令
     *
     * @param key key
     * @return String
     */
    String get(String key) throws RedisConnectException;

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     * @return String
     */
    String set(String key, String value) throws RedisConnectException;

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliscends 毫秒
     * @return String
     */
    String set(String key, String value, Long milliscends) throws RedisConnectException;

    /**
     * del命令
     *
     * @param key key
     * @return Long
     */
    Long del(String... key) throws RedisConnectException;

    /**
     * exists命令
     *
     * @param key key
     * @return Boolean
     */
    Boolean exists(String key) throws RedisConnectException;

    /**
     * pttl命令
     *
     * @param key key
     * @return Long
     */
    Long pttl(String key) throws RedisConnectException;

    /**
     * pexpire命令
     *
     * @param key         key
     * @param milliscends 毫秒
     * @return Long
     */
    Long pexpire(String key, Long milliscends) throws RedisConnectException;


    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param member value
     */
    Long zadd(String key, Double score, String member) throws RedisConnectException;

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     */
    Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException;

    /**
     * zremrangeByScore 命令
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Long
     */
    Long zremrangeByScore(String key, String start, String end) throws RedisConnectException;

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     */
    Long zrem(String key, String... members) throws RedisConnectException;

    /**
     * hset 命令
     *
     * @param key
     * @param fieldKey
     * @param fieldValue
     */
    void hset(String key, String fieldKey, String fieldValue) throws RedisConnectException;

    /**
     * 批量hset 命令
     *
     * @param key
     * @param map
     */
    void batchHset(String key, Map<String, String> map) throws RedisConnectException;


    /**
     * 批量hdel命令
     *
     * @param key
     * @param map
     */
    void batchHdel(String key, Map<String, String> map) throws RedisConnectException;

    /*    *//**
     * 批量srem 命令
     *
     * @param key
     * @param list
     *//*
    void batchSrem(String key, List<String> list) throws RedisConnectException;

    *//**
     * 批量sadd 命令
     *
     * @param key
     * @param list
     *//*
    void batchSadd(String key, List<String> list) throws RedisConnectException;

    *//**
     * 添加set中一个元素
     *
     * @param key
     * @param fieldValue
     * @throws RedisConnectException
     *//*
    Long sadd(String key, String fieldValue) throws RedisConnectException;

    *//**
     * 移除set中一个或多个元素
     *
     * @param key
     * @param fieldKey
     * @throws RedisConnectException
     *//*
    Long srem(String key, String... fieldKey) throws RedisConnectException;*/

    /**
     * hget 命令
     *
     * @param key
     * @param fieldKey
     */
    String hget(String key, String fieldKey) throws RedisConnectException;

    /**
     * hdel 命令
     *
     * @param key
     * @param fieldKey
     */
    void hdel(String key, String fieldKey) throws RedisConnectException;

    /**
     * hgetall 命令
     *
     * @param key
     */
    Map<String, String> hgetall(String key) throws RedisConnectException;

    /**
     * hexits 命令
     *
     * @param key
     * @param fieldKey
     */
    Boolean hexits(String key, String fieldKey) throws RedisConnectException;

    /**
     * hincrby 命令
     *
     * @param key
     * @param fieldKey
     * @param lon
     * @throws RedisConnectException
     */
    void hincrby(String key, String fieldKey, Long lon) throws RedisConnectException;
}
