package com.cecilylove.blueocean.core.enums;

import com.cecilylove.blueocean.core.api.RespCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础通用响应码枚举类
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum CommonRespCode implements RespCode {

    // === 通用状态 ===
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统内部错误，请联系管理员"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或Token过期"),
    FORBIDDEN(403, "无权限访问"),

    /**
     * 业务执行异常 (用于 BizAssertUtils 断言失败或通用的业务报错)
     */
    BUSINESS_ERROR(501, "业务逻辑异常")

    ;

    private final Integer code;
    private final String message;
}