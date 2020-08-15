package com.puhuilink.qbs.example;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRetry
@EnableTransactionManagement
@EnableScheduling
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@MapperScan(basePackages = "com.puhuilink.qbs.**.mapper, com.puhuilink.qbs.example.**.mapper")
@SpringBootApplication(scanBasePackages = {"com.puhuilink.qbs.**", "com.puhuilink.qbs.example.**"}, exclude = {JacksonAutoConfiguration.class})
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
