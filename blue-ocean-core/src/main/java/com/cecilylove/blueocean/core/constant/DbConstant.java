package com.cecilylove.blueocean.core.constant;

/**
 * 数据库常量
 *
 * @author cecilylove
 * @since 1.0.0
 */
public final class DbConstant {

    private DbConstant() {}

    /**
     * 逻辑删除值（String类型，供注解使用）
     */
    public static final String STR_NOT_DELETED = "0";
    public static final String STR_DELETED = "1";
}