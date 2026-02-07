package com.cecilylove.blueocean.web.autoconfig;


import com.cecilylove.blueocean.core.constant.GlobalConfigConstants;
import com.cecilylove.blueocean.web.properties.WebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Web 模块自动装配入口 (聚合模式)
 * 职责：
 * 1. 激活配置属性类 WebProperties
 * 2. 聚合导入各子功能的自动配置类
 * 3. 作为一个总开关，方便在 imports 文件中注册
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnWebApplication
@Import({
    GlobalExceptionHandlerAutoConfiguration.class,
    JacksonAutoConfiguration.class
})
@ConditionalOnProperty(prefix = WebProperties.PREFIX, name = GlobalConfigConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class WebAutoConfiguration {
    // 一个空壳类，就像一个插排，把所有插件都插在这里
}