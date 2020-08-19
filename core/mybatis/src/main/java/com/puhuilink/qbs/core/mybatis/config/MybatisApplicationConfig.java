package com.puhuilink.qbs.core.mybatis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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