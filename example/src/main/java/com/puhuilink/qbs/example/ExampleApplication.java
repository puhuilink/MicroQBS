package com.puhuilink.qbs.example;


import com.puhuilink.qbs.core.web.aop.GlobalResultHandler;
import com.puhuilink.qbs.core.web.config.WebConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@MapperScan(basePackages = "com.puhuilink.qbs.core.**.mapper, com.puhuilink.qbs.example.**.mapper")
@SpringBootApplication(scanBasePackages = {"com.puhuilink.qbs.core.**", "com.puhuilink.qbs.example.**"})
@Import({GlobalResultHandler.class})
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
