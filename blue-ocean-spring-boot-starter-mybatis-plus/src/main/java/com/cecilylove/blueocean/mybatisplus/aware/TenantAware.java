package com.cecilylove.blueocean.mybatisplus.aware;

/**
 * 租户/单位感知接口
 * 实现此接口表示该实体支持多租户/多单位隔离
 * @author Wang Li Hong
 * @since 1.0.0
 */
public interface TenantAware {

    Long getTenantId();

    void setTenantId(Long tenantId);
}
