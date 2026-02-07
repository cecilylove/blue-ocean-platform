package com.cecilylove.blueocean.mybatisplus.properties;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis Plus配置
 * 插件：
 * 自动分页: PaginationInnerInterceptor
 * 多租户: TenantLineInnerInterceptor
 * 动态表名: DynamicTableNameInnerInterceptor
 * 乐观锁: OptimisticLockerInnerInterceptor
 * SQL 性能规范: IllegalSQLInnerInterceptor
 * 防止全表更新与删除: BlockAttackInnerInterceptor
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = MybatisPlusProperties.PREFIX)
public class MybatisPlusProperties {

    public static final String PREFIX = "blue-ocean.mybatis-plus";

    public static final String ENABLE_PAGINATION = "enable-pagination";

    public static final String ENABLE_OPTIMISTIC_LOCKER = "enable-optimistic-locker";

    public static final String ENABLE_BLOCK_ATTACK = "enable-block-attack";

    public static final String ENABLE_TENANT_LINE = "enable-tenant-line";

    /**
     * 是否开启 Mybatis Plus默认配置
     */
    private boolean enabled = true;

    /**
     * 数据库类型
     * 默认 MYSQL
     */
    private DbType dbType = DbType.MYSQL;

    /**
     * 是否开启分页插件
     * 默认 true
     */
    private boolean enablePagination = true;

    /**
     * 是否开启乐观锁
     * 默认 true
     */
    private boolean enableOptimisticLocker = true;

    /**
     * 是否开启防止全表更新/删除插件
     * 默认 true (为了安全，默认开启)
     */
    private boolean enableBlockAttack = true;
}