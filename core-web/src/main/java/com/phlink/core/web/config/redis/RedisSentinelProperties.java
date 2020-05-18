/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:14
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:14
 */
package com.phlink.core.web.config.redis;

import lombok.Data;
import lombok.ToString;

import java.util.List;

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
