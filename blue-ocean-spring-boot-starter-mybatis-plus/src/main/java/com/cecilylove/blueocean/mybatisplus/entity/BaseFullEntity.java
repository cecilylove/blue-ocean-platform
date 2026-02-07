package com.cecilylove.blueocean.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.cecilylove.blueocean.mybatisplus.aware.TenantAware;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseFullEntity extends BaseEntity implements TenantAware {

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 租户ID/单位ID
     */
    private Long tenantId;
}