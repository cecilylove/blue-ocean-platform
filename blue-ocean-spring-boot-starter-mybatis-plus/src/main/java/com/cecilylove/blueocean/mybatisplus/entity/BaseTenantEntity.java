package com.cecilylove.blueocean.mybatisplus.entity;

import com.cecilylove.blueocean.mybatisplus.aware.TenantAware;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseTenantEntity extends BaseEntity implements TenantAware {

    /**
     * 租户ID/单位ID
     */
    private Long tenantId; 
}