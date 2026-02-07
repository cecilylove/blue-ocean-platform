package com.cecilylove.blueocean.web.autoconfig;

import com.cecilylove.blueocean.core.constant.GlobalConfigConstants;
import com.cecilylove.blueocean.web.handler.GlobalExceptionHandler;
import com.cecilylove.blueocean.web.properties.WebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 全局异常处理器自动装配
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@AutoConfiguration // 专门用于自动装配的注解
@ConditionalOnWebApplication // 只有在 Web 环境下才生效（避免在跑 Job/Batch 时报错）
@ConditionalOnProperty(prefix = WebProperties.GlobalExceptionHandlerProperties.PREFIX, name = GlobalConfigConstants.ENABLED, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(WebProperties.class)
public class GlobalExceptionHandlerAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class) // 如果用户自己定义了 GlobalExceptionHandler，就不要加载我们默认的，防止冲突
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
