package com.cecilylove.blueocean.mybatisplus.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.cecilylove.blueocean.core.context.UserContextUtil;
import com.cecilylove.blueocean.core.enums.CommonRespCode;
import com.cecilylove.blueocean.core.exception.BlueOceanBusinessException;
import com.cecilylove.blueocean.mybatisplus.properties.MybatisPlusProperties;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.Set;

/**
 * 默认租户处理器
 * 负责在执行 SQL 时自动追加 WHERE tenant_id = ?
 *
 * @author cecilylove
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class DefaultTenantHandler implements TenantLineHandler {

    private final MybatisPlusProperties properties;

    @Override
    public Expression getTenantId() {
        Long tenantId = UserContextUtil.getTenantId();
        if (tenantId == null) {
            throw new BlueOceanBusinessException(CommonRespCode.UNAUTHORIZED, "租户上下文缺失，请检查登录态或自定义 TenantLineHandler");
        }
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        Set<String> ignoreTables = properties.getTenantIgnoreTables();
        if (tableName == null || ignoreTables == null || ignoreTables.isEmpty()) {
            return false;
        }
        return ignoreTables.stream().anyMatch(tableName::equalsIgnoreCase);
    }

    @Override
    public String getTenantIdColumn() {
        String tenantIdColumn = properties.getTenantIdColumn();
        return hasText(tenantIdColumn) ? tenantIdColumn : "tenant_id";
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
