package com.cecilylove.blueocean.redis.autoconfig;

import com.cecilylove.blueocean.redis.properties.BlueOceanRedisProperties;
import com.cecilylove.blueocean.redis.utils.RedisUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 增强自动配置中心
 * <p>
 * 负责组装增强型的 RedisTemplate (Jackson 序列化) 以及静态工具适配器 RedisUtils。
 * 通过 @AutoConfiguration 确保在 Spring Data Redis 加载后执行。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@ConditionalOnProperty(prefix = BlueOceanRedisProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BlueOceanRedisProperties.class)
public class BlueOceanRedisAutoConfiguration {

    /**
     * 重写并注入自定义序列化策略的 RedisTemplate。
     * 该 Bean 采用了“后发优势”原则：如果用户未定义名为 redisTemplate 的 Bean，则使用此增强版，
     * 以解决 JDK 默认序列化导致的 Key/Value 乱码问题。
     *
     * @param factory      自动注入的 Redis 连接工厂
     * @param objectMapper 自动注入 Spring 容器中的 ObjectMapper (用于保持全局序列化特征一致)
     * @return 配置完毕的 RedisTemplate 实例
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 深拷贝并增强 ObjectMapper 配置，以支持强类型序列化
        ObjectMapper om = (objectMapper != null) ? objectMapper.copy() : new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om, Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // Key/HashKey: 统一使用 String 序列化
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        
        // Value/HashValue: 统一使用基于增强 ObjectMapper 的 Jackson2 序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 注册静态工具适配器 Bean。
     * 采用通配符注入模式 (RedisTemplate<?, ?>)，以确保能适配并代理用户提供的任意自定义 RedisTemplate。
     * 实现“插拔式”设计：优先消费容器中由用户提供的 Bean 实例。
     *
     * @param redisTemplate 当前 Spring 容器中最匹配的 RedisTemplate 驱动 (可能是用户覆盖定义的)
     * @return 初始化完毕且完成静态引用的 RedisUtils 实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUtils redisUtils(RedisTemplate<?, ?> redisTemplate) {
        return new RedisUtils(redisTemplate);
    }

}
