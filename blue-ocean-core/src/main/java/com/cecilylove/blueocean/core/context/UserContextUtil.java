package com.cecilylove.blueocean.core.context;

import java.util.Optional;

/**
 * 用户上下文工具类 (基于 ThreadLocal)
 * 用于在当前线程中存储和获取用户信息
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
public class UserContextUtil {

    private static final ThreadLocal<CurrentUserInfo> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     */
    public static void set(CurrentUserInfo userInfo) {
        THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取当前用户信息
     */
    public static CurrentUserInfo get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 获取当前用户ID (常用便捷方法)
     */
    public static Long getUserId() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(CurrentUserInfo::getUserId)
                .orElse(null);
    }
    
    /**
     * 获取租户ID
     */
    public static Long getTenantId() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(CurrentUserInfo::getTenantId)
                .orElse(null);
    }

    /**
     * 清除上下文 (务必在请求结束时调用，防止内存泄漏)
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}