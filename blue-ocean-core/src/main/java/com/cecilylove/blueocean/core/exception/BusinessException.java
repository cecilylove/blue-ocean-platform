package com.cecilylove.blueocean.core.exception;

import com.cecilylove.blueocean.core.api.RespCode;
import lombok.Getter;

/**
 * 统一业务异常
 * 开发中只要 throw new BusinessException(...) 就会被全局捕获
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    // 直接传枚举
    public BusinessException(RespCode respCode) {
        super(respCode.getMessage());
        this.code = respCode.getCode();
        this.message = respCode.getMessage();
    }

    // 自定义消息（比如：参数错误：手机号格式不对）
    public BusinessException(RespCode respCode, String customMessage) {
        super(customMessage);
        this.code = respCode.getCode();
        this.message = customMessage;
    }
}