/*
 * @Author: sevncz.wen
 * @Date: 2020-08-17 15:13
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-17 15:13
 */
package com.puhuilink.qbs.core.logtrace.config;

import com.puhuilink.qbs.core.logtrace.aop.SystemLogTraceAspect;
import com.puhuilink.qbs.core.logtrace.mapper.LogTraceMapper;
import com.puhuilink.qbs.core.logtrace.service.impl.LogTraceServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-17 15:13
 **/
@Configuration
@MapperScan(basePackageClasses = {
        LogTraceMapper.class
})
@ComponentScan(basePackageClasses = {
        SystemLogTraceAspect.class,
        LogTraceServiceImpl.class
})
public class LogTraceApplicationConfig {
    @Autowired
    ApplicationContext applicationContext;

    public LogTraceApplicationConfig() {
    }

    @PostConstruct
    public void init() {
    }
}
