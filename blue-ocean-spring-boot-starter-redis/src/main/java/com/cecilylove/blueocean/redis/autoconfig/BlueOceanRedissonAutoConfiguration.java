package com.cecilylove.blueocean.redis.autoconfig;

import com.cecilylove.blueocean.redis.properties.BlueOceanRedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 工业级 Redisson 自适应配置桥接
 * <p>
 * 核心逻辑：自动读取 spring.data.redis 的单机、哨兵或集群配置，并智能转换为 Redisson 的 Config。
 * 同时同步 Spring 容器中的 ObjectMapper 以确保序列化闭环，解决分布式环境下对象解析不一致的问题。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration(before = RedissonAutoConfiguration.class)
@ConditionalOnClass(Redisson.class)
@ConditionalOnProperty(prefix = BlueOceanRedisProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class BlueOceanRedissonAutoConfiguration {

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    /**
     * 手动桥接并注册 RedissonClient 实例。
     * 该 Bean 优先级高于官方 Starter 的默认行为，支持全模式自动识别与池化参数对齐。
     *
     * @param redisProperties 自动注入 Spring Data Redis 的核心配置
     * @param objectMapper    自动注入 Spring 容器中的 Jackson 映射集 (用于 Codec)
     * @return 增强且桥接后的 RedissonClient 实例
     */
    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisProperties redisProperties, ObjectMapper objectMapper) {
        Config config = new Config();
        
        // 1. 同步序列化策略：使用 Jackson Codec，并持用同一个 ObjectMapper 对象
        if (objectMapper != null) {
            config.setCodec(new JsonJacksonCodec(objectMapper));
        }

        // 2. 模式探测与桥接
        if (redisProperties.getSentinel() != null) {
            log.info(">>>> [Blue Ocean] Redis 哨兵模式桥接开启...");
            buildSentinelConfig(config, redisProperties);
        } else if (redisProperties.getCluster() != null) {
            log.info(">>>> [Blue Ocean] Redis 集群模式桥接开启...");
            buildClusterConfig(config, redisProperties);
        } else {
            log.info(">>>> [Blue Ocean] Redis 单机模式桥接开启...");
            buildSingleConfig(config, redisProperties);
        }

        // 3. 全局基础参数补充
        config.setNettyThreads(0); // 默认 2 * cores

        return Redisson.create(config);
    }

    /**
     * 桥接构建单机模式配置。
     * 包括连接地址、数据库索引、密码、超时以及 Lettuce 风格的连接池映射。
     */
    private void buildSingleConfig(Config config, RedisProperties redisProperties) {
        String protocol = redisProperties.getSsl().isEnabled() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(protocol + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                .setTimeout((int) redisProperties.getTimeout().toMillis())
                .setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis());

        // 集成 Lettuce 类型的连接池参数
        RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
        if (pool != null) {
            serverConfig.setConnectionMinimumIdleSize(pool.getMinIdle())
                    .setConnectionPoolSize(pool.getMaxActive());
        }
    }

    /**
     * 桥接构建集群模式配置。
     * 包括多节点地址解析、主从读写分离策略模拟及并发超时控制。
     */
    private void buildClusterConfig(Config config, RedisProperties redisProperties) {
        String protocol = redisProperties.getSsl().isEnabled() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
        List<String> nodes = redisProperties.getCluster().getNodes();
        List<String> formattedNodes = new ArrayList<>();
        
        // 自动补齐协议前缀以符合 Redisson 标准
        nodes.forEach(node -> {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                formattedNodes.add(protocol + node);
            } else {
                formattedNodes.add(node);
            }
        });

        config.useClusterServers()
                .addNodeAddress(formattedNodes.toArray(new String[0]))
                .setPassword(redisProperties.getPassword())
                .setTimeout((int) redisProperties.getTimeout().toMillis())
                .setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis());
    }

    /**
     * 桥接构建哨兵模式配置。
     * 绑定主节点名称 (Master Name)，映射集群节点列表并同步数据库索引。
     */
    private void buildSentinelConfig(Config config, RedisProperties redisProperties) {
        String protocol = redisProperties.getSsl().isEnabled() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
        List<String> nodes = redisProperties.getSentinel().getNodes();
        List<String> formattedNodes = new ArrayList<>();
        
        nodes.forEach(node -> {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                formattedNodes.add(protocol + node);
            } else {
                formattedNodes.add(node);
            }
        });

        config.useSentinelServers()
                .setMasterName(redisProperties.getSentinel().getMaster())
                .addSentinelAddress(formattedNodes.toArray(new String[0]))
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                .setTimeout((int) redisProperties.getTimeout().toMillis())
                .setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis());
    }
}
