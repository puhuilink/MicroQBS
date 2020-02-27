package com.phlink.core.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(@Value("classpath:/redisson.yaml") Resource configFile) throws IOException {
        log.info("Redisson 初始化");
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        log.info("CacheManager 初始化");
        return new RedissonSpringCacheManager(redissonClient, "classpath:/cache-config.yaml");
    }

}