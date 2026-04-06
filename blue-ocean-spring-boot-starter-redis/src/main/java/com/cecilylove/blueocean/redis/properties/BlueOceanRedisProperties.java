package com.cecilylove.blueocean.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 基础配置属性
 * 包含对 Redis 增强功能的全局开关。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Getter

@Setter
@ConfigurationProperties(prefix = BlueOceanRedisProperties.PREFIX)
public class BlueOceanRedisProperties {

    public static final String PREFIX = "blue-ocean.redis";

    /**
     * 是否启用 Redis 增强功能 (RedisUtils/RedissonLock)
     */
    private boolean enabled = true;

}
