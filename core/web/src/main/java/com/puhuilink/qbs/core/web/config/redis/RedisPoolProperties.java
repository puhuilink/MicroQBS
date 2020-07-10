/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:15:11
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/redis/RedisPoolProperties.java
 */
package com.puhuilink.qbs.core.web.config.redis;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisPoolProperties {

    private int maxIdle;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int connTimeout;

    private int soTimeout;

    /**
     * 池大小
     */
    private int size;

}
