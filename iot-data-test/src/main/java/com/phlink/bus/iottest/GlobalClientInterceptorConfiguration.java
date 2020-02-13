package com.phlink.bus.iottest;

import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
public class GlobalClientInterceptorConfiguration {

    @Bean
    public GlobalClientInterceptorConfigurer globalInterceptorConfigurerAdapter() {
        return registry -> registry.addClientInterceptors(new LogGrpcInterceptor());
    }

}