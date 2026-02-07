package com.cecilylove.blueocean.core.api;

/**
 * 错误码接口
 * 让所有的枚举类都实现这个接口，方便扩展（比如 AIErrorCode, UserErrorCode）
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
public interface RespCode {

    /**
     * 错误码
     * @return 错误码
     */
    Integer getCode();

    /**
     * 错误信息
     * @return 错误信息
     */
    String getMessage();
}