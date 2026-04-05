package com.cecilylove.blueocean.core.utils;



import com.cecilylove.blueocean.core.api.RespCode;
import com.cecilylove.blueocean.core.exception.BlueOceanBusinessException;

import java.util.Collection;

/**
 * 业务断言工具类
 * 类似于 JUnit 的 Assert，但是抛出的是 BlueOceanBusinessException
 *
 * @author cecilylove
 * @since 1.0.0
 */
public final class BizAssertUtils {

    private BizAssertUtils() {}

    /**
     * 对象不能为空
     * @param object 目标对象
     * @param respCode 错误码
     */
    public static void notNull(Object object, RespCode respCode) {
        if (object == null) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 对象必须为空
     * @param object 目标对象
     * @param respCode 错误码
     */
    public static void isNull(Object object, RespCode respCode) {
        if (object != null) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 字符串不能为空
     * @param text 目标字符串
     * @param respCode 错误码
     */
    public static void notBlank(String text, RespCode respCode) {
        if (text == null || text.trim().isEmpty()) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 字符串必须为空
     * @param text 目标字符串
     * @param respCode 错误码
     */
    public static void isBlank(String text, RespCode respCode) {
        if (!(text == null || text.trim().isEmpty())) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 单列集合不能为空
     * @param collection 目标集合
     * @param respCode 错误码
     */
    public static void notEmpty(Collection<?> collection, RespCode respCode) {
        if (collection == null || collection.isEmpty()) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 单列集合必须为空
     * @param collection 目标集合
     * @param respCode 错误码
     */
    public static void isEmpty(Collection<?> collection, RespCode respCode) {
        if (!(collection == null || collection.isEmpty())) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 判断表达式是否为 True
     * @param expression 目标表达式
     * @param respCode 错误码
     */
    public static void isTrue(boolean expression, RespCode respCode) {
        if (!expression) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 判断表达式是否为 False
     * @param expression 目标表达式
     * @param respCode 错误码
     */
    public static void isFalse(boolean expression, RespCode respCode) {
        if (expression) {
            throw new BlueOceanBusinessException(respCode);
        }
    }
}
