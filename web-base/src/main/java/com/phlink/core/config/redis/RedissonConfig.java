package com.phlink.core.config.redis;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {
    @Autowired
    RedisProperties redisProperties;

    @Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${spring.redis.mode}'=='single' or '${spring.redis.mode}'=='cluster' or '${spring.redis.mode}'=='sentinel'")
    protected class RedissonSingleClientConfiguration {

        /**
         * 单机模式 redisson 客户端
         */

        @Bean(destroyMethod = "shutdown")
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
        RedissonClient redissonSingle() throws Exception {
            Config config = new Config();
            Codec codec = (Codec) ClassUtils.forName(redisProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
            config.setCodec(codec);
            config.setEventLoopGroup(new NioEventLoopGroup());
            String node = redisProperties.getSingle().getAddress();
            node = node.startsWith("redis://") ? node : "redis://" + node;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(redisProperties.getPool().getConnTimeout())
                    .setConnectionPoolSize(redisProperties.getPool().getSize())
                    .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
            if (StrUtil.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
        public CacheManager cacheManager(RedissonClient redissonSingle) {
            log.info("CacheManager 初始化");
            return new RedissonSpringCacheManager(redissonSingle, "classpath:/cache-config.yaml");
        }


        /**
         * 集群模式的 redisson 客户端
         *
         * @return
         */
        @Bean(destroyMethod = "shutdown")
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
        RedissonClient redissonCluster() throws Exception {
            log.info("cluster redisProperties:" + redisProperties.getCluster());

            Config config = new Config();
            Codec codec = (Codec) ClassUtils.forName(redisProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
            config.setCodec(codec);
            config.setEventLoopGroup(new NioEventLoopGroup());
            String[] nodes = redisProperties.getCluster().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setScanInterval(
                            redisProperties.getCluster().getScanInterval())
                    .setIdleConnectionTimeout(
                            redisProperties.getPool().getSoTimeout())
                    .setConnectTimeout(
                            redisProperties.getPool().getConnTimeout())
                    .setFailedSlaveCheckInterval(
                            redisProperties.getCluster().getFailedAttempts())
                    .setRetryAttempts(
                            redisProperties.getCluster().getRetryAttempts())
                    .setRetryInterval(
                            redisProperties.getCluster().getRetryInterval())
                    .setMasterConnectionPoolSize(redisProperties.getCluster()
                            .getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redisProperties.getCluster()
                            .getSlaveConnectionPoolSize())
                    .setTimeout(redisProperties.getTimeout());
            if (StrUtil.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
        public CacheManager cacheManagerCluster(RedissonClient redissonCluster) {
            log.info("CacheManager 初始化");
            return new RedissonSpringCacheManager(redissonCluster, "classpath:/cache-config.yaml");
        }

        /**
         * 哨兵模式 redisson 客户端
         *
         * @return
         */

        @Bean(destroyMethod = "shutdown")
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
        RedissonClient redissonSentinel() throws Exception {
            log.info("sentinel redisProperties:" + redisProperties.getSentinel());
            Config config = new Config();
            Codec codec = (Codec) ClassUtils.forName(redisProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
            config.setCodec(codec);
            config.setEventLoopGroup(new NioEventLoopGroup());
            String[] nodes = redisProperties.getSentinel().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(newNodes.toArray(new String[0]))
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .setReadMode(ReadMode.SLAVE)
                    .setFailedAttempts(redisProperties.getSentinel().getFailMax())
                    .setTimeout(redisProperties.getTimeout())
                    .setMasterConnectionPoolSize(redisProperties.getPool().getSize())
                    .setSlaveConnectionPoolSize(redisProperties.getPool().getSize());

            if (StrUtil.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }

            return Redisson.create(config);
        }

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
        public CacheManager cacheManagerSentinel(RedissonClient redissonSentinel) {
            log.info("CacheManager 初始化");
            return new RedissonSpringCacheManager(redissonSentinel, "classpath:/cache-config.yaml");
        }
    }
}