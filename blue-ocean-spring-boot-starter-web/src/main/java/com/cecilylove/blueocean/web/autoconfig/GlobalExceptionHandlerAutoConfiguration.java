package com.cecilylove.blueocean.web.autoconfig;

import com.cecilylove.blueocean.core.constant.GlobalConfigConstants;
import com.cecilylove.blueocean.web.handler.GlobalExceptionHandler;
import com.cecilylove.blueocean.web.properties.WebProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局异常处理器自动装配
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = WebProperties.GlobalExceptionHandlerProperties.PREFIX, name = GlobalConfigConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class GlobalExceptionHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class) // 如果用户自己定义了 GlobalExceptionHandler，就不要加载我们默认的，防止冲突
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
