/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:14:50
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/redis/RedisSentinelProperties.java
 */
package com.phlink.core.web.config.redis;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisSentinelProperties {

    /**
     * 哨兵master 名称
     */
    private String master;

    /**
     * 哨兵节点
     */
    private List<String> nodes;

    /**
     * 哨兵配置
     */
    private boolean masterOnlyWrite;

    /**
     *
     */
    private int failMax;
}
