package com.cecilylove.blueocean.core.enums;

import com.cecilylove.blueocean.core.constant.DbConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局逻辑删除状态枚举
 *
 * @author  Wang Li Hong
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    // 这里引用常量，实现统一管理
    NOT_DELETED(Integer.parseInt(DbConstant.STR_NOT_DELETED), "未删除"),

    DELETED(Integer.parseInt(DbConstant.STR_DELETED), "已删除");

    private final Integer code;
    private final String desc;
}