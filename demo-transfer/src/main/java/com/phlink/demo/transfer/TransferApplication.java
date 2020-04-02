/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:19
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-02 14:55:11
 */
package com.phlink.demo.transfer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
// 启用自带定时任务
@EnableScheduling
@EnableAsync
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
// 启用Admin监控
@EnableAdminServer
@MapperScan(basePackages = "com.phlink.core.**.mapper, com.phlink.demo.transfer.**.mapper")
public class TransferApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TransferApplication.class).run(args);
    }

}
