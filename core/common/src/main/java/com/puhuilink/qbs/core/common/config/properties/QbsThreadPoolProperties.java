/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 15:26
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 15:26
 */
package com.puhuilink.qbs.core.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-13 15:26
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qbs.threadpool")
public class QbsThreadPoolProperties {
    /**
     * 核心线程
     */
    private int corePoolSize = 8;
    /**
     * 最大线程
     */
    private int maxPoolSize = Integer.MAX_VALUE;
    /**
     * 缓冲队列
     */
    private int queueCapacity = Integer.MAX_VALUE;
    /**
     * 允许线程空闲时间（单位：默认为秒)
     */
    private int keepAliveSeconds = 60;
    /**
     * 线程池前缀
     */
    private String threadName = "QbsAsyncThread-";
}
