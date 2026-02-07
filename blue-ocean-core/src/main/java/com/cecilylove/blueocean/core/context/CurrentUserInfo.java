package com.cecilylove.blueocean.core.context;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 当前用户信息
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Data
public class CurrentUserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色
     */
    private String role;

    /**
     * 用户名
     */
    private String username;

    /**
     * 租户ID (多租户场景)
     */
    private Long tenantId;

    /**
     * 扩展字段 (用于存储一些非标准数据，作为兜底)
     */
    private Map<String, Object> ext;

    public Map<String, Object> getExt() {
        return ext == null ? Collections.emptyMap() : ext;
    }
}
