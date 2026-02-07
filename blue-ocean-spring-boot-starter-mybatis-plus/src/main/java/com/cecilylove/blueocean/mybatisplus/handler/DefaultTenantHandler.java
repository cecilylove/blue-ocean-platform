package com.cecilylove.blueocean.mybatisplus.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.cecilylove.blueocean.core.context.UserContextUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 默认租户处理器
 * 负责在执行 SQL 时自动追加 WHERE tenant_id = ?
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
public class DefaultTenantHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        Long tenantId = UserContextUtil.getTenantId();
        if (tenantId == null) {
            // 如果没拿租户ID（比如后台任务或未登录），给个默认值 0
            return new LongValue(0L);
        }
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 默认不忽略任何表，建议在业务配置里做过滤
        // 或者在这里写死一些系统表，比如 "sys_user", "sys_role" 等不需要隔离的表
        return false;
    }

    @Override
    public String getTenantIdColumn() {
        // 默认数据库字段名叫 tenant_id
        return "tenant_id";
    }
}