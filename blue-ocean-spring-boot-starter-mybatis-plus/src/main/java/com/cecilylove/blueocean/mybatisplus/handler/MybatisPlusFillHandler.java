package com.cecilylove.blueocean.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cecilylove.blueocean.core.context.UserContextUtil;
import com.cecilylove.blueocean.core.enums.DeleteStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充处理器
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Slf4j
public class MybatisPlusFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 准备数据
        Long userId = UserContextUtil.getUserId();
        // 如果没登录（比如后台定时任务），给个默认uid，防止数据库报错
        if (userId == null) {
            userId = 0L;
        }

        LocalDateTime now = LocalDateTime.now();

        // 2. 填充基础字段
        this.strictInsertFill(metaObject, "createdBy", Long.class, userId);
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updatedBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, now);

        // 3. 填充逻辑删除 (如果存在 deleted 字段)
        this.strictInsertFill(metaObject, "deleted", Integer.class, DeleteStatusEnum.NOT_DELETED.getCode());

        // 4. 填充多租户 (如果存在 tenantId 字段)
        Long tenantId = UserContextUtil.getTenantId();
        if (tenantId != null) {
            this.strictInsertFill(metaObject, "tenantId", Long.class, tenantId);
        }

        // 5. 填充乐观锁 (如果存在 version 字段)
        this.strictInsertFill(metaObject, "version", Integer.class, 0);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = UserContextUtil.getUserId();
        if (userId == null) {
            userId = 0L;
        }
        // setFieldValByName 会强制覆盖，不管原值是不是 null，这通常是更新时间的预期行为
        this.setFieldValByName("updatedBy", userId, metaObject);
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
    }
}
