package com.cecilylove.blueocean.core.utils;



import com.cecilylove.blueocean.core.api.RespCode;
import com.cecilylove.blueocean.core.exception.BlueOceanBusinessException;

import java.util.Collection;

/**
 * 业务断言工具类
 * <p>
 * 封装常用的业务校验逻辑，若校验不通过则统一抛出 {@link BlueOceanBusinessException}。
 * 旨在通过简单的语义化方法替代繁琐的 if-throw 结构。
 *
 * @author cecilylove
 * @since 1.0.0
 */
public final class BizAssertUtils {

    private BizAssertUtils() {
    }

    /**
     * 断言对象不能为空
     *
     * @param object   待校验的目标对象
     * @param respCode 校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当对象为 null 时抛出此异常
     */
    public static void notNull(Object object, RespCode respCode) {
        if (object == null) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言对象不能为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param object  待校验的目标对象
     * @param message 校验失败时的错误消息 (支持 {} 占位符)
     * @param args    占位符填充参数
     * @throws BlueOceanBusinessException 当对象为 null 时抛出此异常
     */
    public static void notNull(Object object, String message, Object... args) {
        if (object == null) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言对象不能为空，并指定固定错误消息
     *
     * @param object  待校验的目标对象
     * @param message 校验失败时的错误消息
     * @throws BlueOceanBusinessException 当对象为 null 时抛出此异常
     */
    public static void notNull(Object object, String message) {
        notNull(object, message, (Object[]) null);
    }

    /**
     * 断言对象必须为空
     *
     * @param object   待校验的目标对象
     * @param respCode 校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当对象不为 null 时抛出此异常
     */
    public static void isNull(Object object, RespCode respCode) {
        if (object != null) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言对象必须为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param object  待校验的目标对象
     * @param message 校验失败时的错误消息 (支持 {} 占位符)
     * @param args    占位符填充参数
     * @throws BlueOceanBusinessException 当对象不为 null 时抛出此异常
     */
    public static void isNull(Object object, String message, Object... args) {
        if (object != null) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言对象必须为空，并指定固定错误消息
     *
     * @param object  待校验的目标对象
     * @param message 校验失败时的错误消息
     * @throws BlueOceanBusinessException 当对象不为 null 时抛出此异常
     */
    public static void isNull(Object object, String message) {
        isNull(object, message, (Object[]) null);
    }

    /**
     * 断言字符串不能为空 (含空字符串与空格字符串校验)
     *
     * @param text     待校验的目标字符串
     * @param respCode 校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当字符串为空或仅包含空白字符时抛出此异常
     */
    public static void notBlank(String text, RespCode respCode) {
        if (text == null || text.trim().isEmpty()) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言字符串不能为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param text    待校验的目标字符串
     * @param message 校验失败时的错误消息 (支持 {} 占位符)
     * @param args    占位符填充参数
     * @throws BlueOceanBusinessException 当字符串为空或仅包含空白字符时抛出此异常
     */
    public static void notBlank(String text, String message, Object... args) {
        if (text == null || text.trim().isEmpty()) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言字符串不能为空，并指定固定错误消息
     *
     * @param text    待校验的目标字符串
     * @param message 校验失败时的错误消息
     * @throws BlueOceanBusinessException 当字符串为空或仅包含空白字符时抛出此异常
     */
    public static void notBlank(String text, String message) {
        notBlank(text, message, (Object[]) null);
    }

    /**
     * 断言字符串必须为空
     *
     * @param text     待校验的目标字符串
     * @param respCode 校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当字符串不为空时抛出此异常
     */
    public static void isBlank(String text, RespCode respCode) {
        if (!(text == null || text.trim().isEmpty())) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言字符串必须为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param text    待校验的目标字符串
     * @param message 校验失败时的错误消息 (支持 {} 占位符)
     * @param args    占位符填充参数
     * @throws BlueOceanBusinessException 当字符串不为空时抛出此异常
     */
    public static void isBlank(String text, String message, Object... args) {
        if (!(text == null || text.trim().isEmpty())) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言字符串必须为空，并指定固定错误消息
     *
     * @param text    待校验的目标字符串
     * @param message 校验失败时的错误消息
     * @throws BlueOceanBusinessException 当字符串不为空时抛出此异常
     */
    public static void isBlank(String text, String message) {
        isBlank(text, message, (Object[]) null);
    }

    /**
     * 断言单列集合不能为空
     *
     * @param collection 待校验的目标集合
     * @param respCode   校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当集合为 null 或不包含任何元素时抛出此异常
     */
    public static void notEmpty(Collection<?> collection, RespCode respCode) {
        if (collection == null || collection.isEmpty()) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言单列集合不能为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param collection 待校验的目标集合
     * @param message    校验失败时的错误消息 (支持 {} 占位符)
     * @param args       占位符填充参数
     * @throws BlueOceanBusinessException 当集合为 null 或不包含任何元素时抛出此异常
     */
    public static void notEmpty(Collection<?> collection, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言单列集合不能为空，并指定固定错误消息
     *
     * @param collection 待校验的目标集合
     * @param message    校验失败时的错误消息
     * @throws BlueOceanBusinessException 当集合为 null 或不包含任何元素时抛出此异常
     */
    public static void notEmpty(Collection<?> collection, String message) {
        notEmpty(collection, message, (Object[]) null);
    }

    /**
     * 断言单列集合必须为空
     *
     * @param collection 待校验的目标集合
     * @param respCode   校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当集合不为 null 且包含元素时抛出此异常
     */
    public static void isEmpty(Collection<?> collection, RespCode respCode) {
        if (!(collection == null || collection.isEmpty())) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言单列集合必须为空，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param collection 待校验的目标集合
     * @param message    校验失败时的错误消息 (支持 {} 占位符)
     * @param args       占位符填充参数
     * @throws BlueOceanBusinessException 当集合不为 null 且包含元素时抛出此异常
     */
    public static void isEmpty(Collection<?> collection, String message, Object... args) {
        if (!(collection == null || collection.isEmpty())) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言单列集合必须为空，并指定固定错误消息
     *
     * @param collection 待校验的目标集合
     * @param message    校验失败时的错误消息
     * @throws BlueOceanBusinessException 当集合不为 null 且包含元素时抛出此异常
     */
    public static void isEmpty(Collection<?> collection, String message) {
        isEmpty(collection, message, (Object[]) null);
    }

    /**
     * 断言表达式是否为 True
     *
     * @param expression 待校验的布尔表达式
     * @param respCode   校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当表达式结果为 false 时抛出此异常
     */
    public static void isTrue(boolean expression, RespCode respCode) {
        if (!expression) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言表达式是否为 True，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param expression 待校验的布尔表达式
     * @param message    校验失败时的错误消息 (支持 {} 占位符)
     * @param args       占位符填充参数
     * @throws BlueOceanBusinessException 当表达式结果为 false 时抛出此异常
     */
    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言表达式是否为 True，并指定固定错误消息
     *
     * @param expression 待校验的布尔表达式
     * @param message    校验失败时的错误消息
     * @throws BlueOceanBusinessException 当表达式结果为 false 时抛出此异常
     */
    public static void isTrue(boolean expression, String message) {
        isTrue(expression, message, (Object[]) null);
    }

    /**
     * 断言表达式是否为 False
     *
     * @param expression 待校验的布尔表达式
     * @param respCode   校验失败时抛出的错误码枚举
     * @throws BlueOceanBusinessException 当表达式结果为 true 时抛出此异常
     */
    public static void isFalse(boolean expression, RespCode respCode) {
        if (expression) {
            throw new BlueOceanBusinessException(respCode);
        }
    }

    /**
     * 断言表达式是否为 False，并指定自定义错误消息 (支持 {} 占位符)
     *
     * @param expression 待校验的布尔表达式
     * @param message    校验失败时的错误消息 (支持 {} 占位符)
     * @param args       占位符填充参数
     * @throws BlueOceanBusinessException 当表达式结果为 true 时抛出此异常
     */
    public static void isFalse(boolean expression, String message, Object... args) {
        if (expression) {
            throw new BlueOceanBusinessException(message, args);
        }
    }

    /**
     * 断言表达式是否为 False，并指定固定错误消息
     *
     * @param expression 待校验的布尔表达式
     * @param message    校验失败时的错误消息
     * @throws BlueOceanBusinessException 当表达式结果为 true 时抛出此异常
     */
    public static void isFalse(boolean expression, String message) {
        isFalse(expression, message, (Object[]) null);
    }
}

