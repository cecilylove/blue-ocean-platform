package com.cecilylove.blueocean.mybatisplus.autoconfig;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.cecilylove.blueocean.core.constant.GlobalConfigConstants;
import com.cecilylove.blueocean.mybatisplus.handler.DefaultTenantHandler;
import com.cecilylove.blueocean.mybatisplus.handler.MybatisPlusFillHandler;
import com.cecilylove.blueocean.mybatisplus.properties.MybatisPlusProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * Mybatis Plus 自动装配
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnClass(MybatisPlusInterceptor.class) // 有 MP 的包才加载
@EnableConfigurationProperties(MybatisPlusProperties.class)
@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = GlobalConfigConstants.ENABLED, havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class MybatisPlusAutoConfiguration {

    private final MybatisPlusProperties properties;

    /**
     * 自动填充处理器
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MybatisPlusFillHandler mybatisPlusFillHandler() {
        return new MybatisPlusFillHandler();
    }

    /**
     * 注册 Mybatis Plus 插件拦截器
     * @param interceptors 拦截器列表
     * @return MybatisPlusInterceptor
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<InnerInterceptor> interceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 按照 @Order 排序后依次添加
        interceptors.orderedStream().forEach(interceptor::addInnerInterceptor);
        return interceptor;
    }

    /**
     * 多租户,当容器中没有用户自定义的 TenantLineHandler 时，注入默认的
     */
    @Bean
    @ConditionalOnMissingBean(TenantLineHandler.class)
    @ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = MybatisPlusProperties.ENABLE_TENANT_LINE, havingValue = "true", matchIfMissing = true)
    public TenantLineHandler tenantLineHandler() {
        return new DefaultTenantHandler();
    }

    @Bean
    @Order(1)
    @ConditionalOnBean(TenantLineHandler.class)
    @ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = MybatisPlusProperties.ENABLE_TENANT_LINE, havingValue = "true", matchIfMissing = true)
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
        TenantLineInnerInterceptor interceptor = new TenantLineInnerInterceptor();
        interceptor.setTenantLineHandler(tenantLineHandler);
        return interceptor;
    }

    /**
     * 乐观锁插件
     */
    @Bean
    @Order(10)
    @ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = MybatisPlusProperties.ENABLE_OPTIMISTIC_LOCKER, havingValue = "true", matchIfMissing = true)
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 分页插件
     */
    @Bean
    @Order(20)
    @ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = MybatisPlusProperties.ENABLE_PAGINATION, havingValue = "true", matchIfMissing = true)
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor(properties.getDbType());
    }


    /**
     * 防全表更新与删除插件
     */
    @Bean
    @Order(30)
    @ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = MybatisPlusProperties.ENABLE_BLOCK_ATTACK, havingValue = "true", matchIfMissing = true)
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }
}