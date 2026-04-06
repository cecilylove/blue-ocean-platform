package com.cecilylove.blueocean.core.exception;

import cn.hutool.core.util.StrUtil;
import com.cecilylove.blueocean.core.api.RespCode;
import com.cecilylove.blueocean.core.enums.CommonRespCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 统一业务异常
 * 开发中只要 throw new BlueOceanBusinessException(...) 就会被全局捕获
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Getter
public class BlueOceanBusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 默认错误码使用 CommonRespCode.BUSINESS_ERROR
     */
    private static final Integer DEFAULT_ERROR_CODE = CommonRespCode.BUSINESS_ERROR.getCode();

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误信息
     */
    private final String message;

    /**
     * 直接传递错误消息构造异常 (使用默认错误码 501)
     *
     * @param message 错误消息
     */
    public BlueOceanBusinessException(String message) {
        super(message);
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
    }

    /**
     * 使用占位符动态构造错误信息 (使用默认错误码 501)
     * 支持 SLF4J 风格的 {} 占位符
     *
     * @param message 消息模板 (支持 {} 占位符)
     * @param args    用于填充消息模板的参数列表
     */
    public BlueOceanBusinessException(String message, Object... args) {
        super(StrUtil.format(message, args));
        this.code = DEFAULT_ERROR_CODE;
        this.message = StrUtil.format(message, args);
    }

    /**
     * 直接传递错误码枚举构造异常
     *
     * @param respCode 错误码枚举
     */
    public BlueOceanBusinessException(RespCode respCode) {
        super(respCode.getMessage());
        this.code = respCode.getCode();
        this.message = respCode.getMessage();
    }

    /**
     * 传递错误码枚举并自定义详细错误信息
     *
     * @param respCode      错误码枚举
     * @param customMessage 自定义错误描述
     */
    public BlueOceanBusinessException(RespCode respCode, String customMessage) {
        super(customMessage);
        this.code = respCode.getCode();
        this.message = customMessage;
    }

    /**
     * 传递错误码枚举并使用占位符动态构造错误信息
     * 支持 SLF4J 风格的 {} 占位符
     *
     * @param respCode 错误码枚举
     * @param args     用于填充消息模板的参数列表
     */
    public BlueOceanBusinessException(RespCode respCode, Object... args) {
        super(StrUtil.format(respCode.getMessage(), args));
        this.code = respCode.getCode();
        this.message = StrUtil.format(respCode.getMessage(), args);
    }

    /**
     * 直接传递错误码与消息模板动态构造错误信息
     * 支持 SLF4J 风格的 {} 占位符
     *
     * @param code    自定义错误码
     * @param message 消息模板 (支持 {} 占位符)
     * @param args    填充参数
     */
    public BlueOceanBusinessException(Integer code, String message, Object... args) {
        super(StrUtil.format(message, args));
        this.code = code;
        this.message = StrUtil.format(message, args);
    }

    /**
     * 构造异常并包含原始异常
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BlueOceanBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
    }

    /**
     * 构造异常并包含原始异常 (使用错误码枚举)
     *
     * @param respCode 错误码枚举
     * @param cause    原始异常
     */
    public BlueOceanBusinessException(RespCode respCode, Throwable cause) {
        super(respCode.getMessage(), cause);
        this.code = respCode.getCode();
        this.message = respCode.getMessage();
    }
}
