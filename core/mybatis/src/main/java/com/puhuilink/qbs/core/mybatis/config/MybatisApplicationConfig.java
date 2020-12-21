/*
 * @Author: sevncz.wen
 * @Date: 2020-08-19 17:24:35
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-10-30 16:47:21
 * @FilePath: /MicroQBS/core/mybatis/src/main/java/com/puhuilink/qbs/core/mybatis/config/MybatisApplicationConfig.java
 */
package com.puhuilink.qbs.core.mybatis.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisApplicationConfig {
    @Autowired
    ApplicationContext applicationContext;

    public MybatisApplicationConfig() {
    }

    @PostConstruct
    public void init() {
    }
}
