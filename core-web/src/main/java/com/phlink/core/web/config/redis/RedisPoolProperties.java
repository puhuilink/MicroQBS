/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:06
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:06
 */
package com.phlink.core.web.config.redis;

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
