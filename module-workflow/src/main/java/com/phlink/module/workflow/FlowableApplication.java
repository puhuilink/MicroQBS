/*
 * @Author: sevncz.wen
 * @Date: 2020-03-30 11:21:11
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-03-30 11:21:11
 */
package com.phlink.module.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wen
 */
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@MapperScan(basePackages = "com.phlink.**.mapper")
public class FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }
}
