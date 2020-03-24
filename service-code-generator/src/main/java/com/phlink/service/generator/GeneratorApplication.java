package com.phlink.service.generator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
// 启用Admin监控
@EnableAdminServer
@MapperScan(basePackages = "com.phlink.core.**.mapper, com.phlink.service.**.mapper")
public class GeneratorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GeneratorApplication.class)
                .run(args);
    }
}
