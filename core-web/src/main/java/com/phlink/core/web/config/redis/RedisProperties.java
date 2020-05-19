/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:14:58
 * @FilePath: /phlink-common-framework/core-web/src/main/java/com/phlink/core/web/config/redis/RedisProperties.java
 */
package com.phlink.core.web.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.ToString;

@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = false)
@Data
@ToString
public class RedisProperties {

    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    private String password;

    private String mode;

    private String codec;

    /**
     * 池配置
     */
    private RedisPoolProperties pool;

    /**
     * 单机信息配置
     */
    private RedisSingleProperties single;

    /**
     * 集群 信息配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;
}
