package com.cecilylove.blueocean.core.api;


import com.cecilylove.blueocean.core.enums.CommonRespCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应结果封装
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 是否成功
     */
    private boolean success;

    // 强制使用静态方法
    protected Result() {}

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(CommonRespCode.SUCCESS.getCode());
        result.setMessage(CommonRespCode.SUCCESS.getMessage());
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> error(RespCode respCode) {
        return error(respCode.getCode(), respCode.getMessage());
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}