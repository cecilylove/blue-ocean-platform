package com.cecilylove.blueocean.framework.autoconfig;

import com.cecilylove.blueocean.framework.spring.SpringUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 框架层自动装配
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@AutoConfiguration
public class FrameworkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SpringUtil.class)
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}