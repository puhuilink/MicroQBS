package com.puhuilink.qbs.example;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
// 启用自带定时任务
@EnableScheduling
@EnableAsync
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
// 启用Admin监控
@MapperScan(basePackages = "com.puhuilink.qbs.core.**.mapper, com.puhuilink.qbs.example.**.mapper")
@SpringBootApplication(scanBasePackages={"com.puhuilink.qbs.core.*", "com.puhuilink.qbs.example.*"})
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
