package com.cecilylove.blueocean.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 基础工具类 (静态适配器 Bean)
 * <p>
 * 提供对 Redis 的常用技术封装（String, Hash, Set, List 等），支持静态方法直接调用。
 * 本类作为 Spring 上下文中的一个 Bean 实例存在，旨在通过 Spring 容器自动桥接最合适的 RedisTemplate。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Slf4j
public class RedisUtils {

    /**
     * 持有 RedisTemplate 的静态引用，由构造函数由 Spring 容器初始化
     */
    private static RedisTemplate<Object, Object> redisTemplate;

    /**
     * 框架级构造注入。
     * 采用通配符 RedisTemplate<?, ?> 注入，以确保能适配用户提供的任意泛型变体（插拔式设计）。
     *
     * @param redisTemplate 自动注入的 RedisTemplate 实例
     */
    @SuppressWarnings("unchecked")
    public RedisUtils(RedisTemplate<?, ?> redisTemplate) {
        // 内部强制转换为最通用的 <Object, Object> 模式，确保最大兼容性
        RedisUtils.redisTemplate = (RedisTemplate<Object, Object>) redisTemplate;
    }

    /**
     * 获取当前底层驱动的实例引用。
     * 该方法的存在的首要目的是使本类在 IDE 检查中被识别为“状态 Bean”，从而消除对纯静态工具类实例化的警告。
     *
     * @return 底层使用的 RedisTemplate 实例
     */
    public RedisTemplate<Object, Object> template() {
        return redisTemplate;
    }

    // ============================= Common =============================

    /**
     * 为指定的 Key 设置过期时间
     *
     * @param key  缓存键名
     * @param time 过期时长 (单位: 秒)，若小于等于 0 则设置立即过期
     * @return true 表示成功，false 表示失败或异常
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis 写入过期时间异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取指定 Key 的剩余过期时间
     *
     * @param key 缓存键名
     * @return 剩余时间 (单位: 秒)。若返回 -1 表示永久有效；返回 -2 表示 Key 不存在
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 检查指定的 Key 是否预测存在于 Redis 中
     *
     * @param key 缓存键名
     * @return true 表示存在，false 表示不存在或操作异常
     */
    public static Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis 检查 Key 是否存在异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 批量删除指定的缓存 Key
     *
     * @param key 变长参数，指定一个或多个要删除的键名
     */
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    // ============================ String =============================

    /**
     * 获取指定 Key 的缓存值并强制转换为目标类型
     *
     * @param <T> 目标类型泛型
     * @param key 缓存键名
     * @return 转换后的值对象。若不存在则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入一个永久存储的缓存项
     *
     * @param key   缓存键名
     * @param value 缓存值对象 (将被 Jackson 编码)
     * @return true 表示操作成功
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis 写入永久缓存异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 写入一个带失效时间的缓存项
     *
     * @param key   缓存键名
     * @param value 缓存值对象
     * @param time  失效时长 (秒)，若小于等于 0 则降级为永久存储
     * @return true 表示操作成功
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis 写入有效时间缓存异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 数值型 Key 递增
     *
     * @param key   缓存键名 (值需为数字型)
     * @param delta 递增步长 (需为正数)
     * @return 递增后的新结果
     */
    public static Long incr(String key, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("Redis 递增因子 [delta] 必须大于 0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 数值型 Key 递减
     *
     * @param key   缓存键名 (值需为数字型)
     * @param delta 递减步长 (需为正数)
     * @return 递减后的新结果
     */
    public static Long decr(String key, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("Redis 递减因子 [delta] 必须大于 0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================ Hash =================================

    /**
     * 向 Hash 中获取指定项的值
     *
     * @param key  Hash 键名
     * @param item 项目键名
     * @return 对应的项目值
     */
    public static Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取 Hash 全量 Map
     *
     * @param key Hash 键名
     * @return 成员 Map
     */
    public static Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向 Hash 中批量存入数据 (Map)
     *
     * @param key 缓存键名
     * @param map 包含多个键值对的 Map
     * @return true 表示操作成功
     */
    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis 批量写入 Hash 异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 向 Hash 中存入一个单项
     *
     * @param key   缓存键名
     * @param item  项目键名
     * @param value 项目值
     * @return true 表示操作成功
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Redis 写入单一 Hash 异常: key={}, item={}", key, item, e);
            return false;
        }
    }

    /**
     * 批量删除 Hash 中的指定项
     *
     * @param key   Hash 键名
     * @param items 待删除的项目键
     * @return 删除成功的项目数
     */
    public static Long hdel(String key, Object... items) {
        return redisTemplate.opsForHash().delete(key, items);
    }

    /**
     * 判定 Hash 中是否存在某项
     *
     * @param key  Hash 键名
     * @param item 项目键名
     * @return true 表示存在
     */
    public static Boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    // ============================ Set ==============================

    /**
     * 获取 Set 全量成员
     *
     * @param key 缓存键名
     * @return 成员 Set 集合
     */
    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis 读取 Set 内容异常: key={}", key, e);
            return null;
        }
    }

    /**
     * 检查某个值是否在 Set 成员中
     *
     * @param key   缓存键名
     * @param value 判定对象
     * @return true 表示是其成员
     */
    public static Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("Redis 检查 Set 成员异常: key={}", key, e);
            return false;
        }
    }

    /**
     * 向 Set 中添加成员
     *
     * @param key    缓存键名
     * @param values 待添加的成员
     * @return 添加成功的个数
     */
    public static Long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Redis 写入 Set 异常: key={}", key, e);
            return 0L;
        }
    }

    /**
     * 移除 Set 中的指定成员
     *
     * @param key    缓存键名
     * @param values 待移除的成员
     * @return 成功移除的个数
     */
    public static Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("Redis 移除 Set 成员异常: key={}", key, e);
            return 0L;
        }
    }

    // =============================== List ==================================

    /**
     * 从列表左侧 (头部) 压入数据
     *
     * @param key   缓存键名
     * @param value 存入的对象
     * @return 压入后列表的长度
     */
    public static Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从列表右侧 (尾部) 压入数据
     *
     * @param key   缓存键名
     * @param value 存入的对象
     * @return 压入后列表的长度
     */
    public static Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从列表左侧 (头部) 弹出并移除一个数据
     *
     * @param key 缓存键名
     * @return 弹出的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T lLeftPop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从列表右侧 (尾部) 弹出并移除一个数据
     *
     * @param key 缓存键名
     * @return 弹出的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T lRightPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 基于索引范围查询 List 缓存内容
     *
     * @param key   缓存键名
     * @param start 起始索引 (0 表示首个元素)
     * @param end   结束索引 (-1 表示最后一个元素)
     * @return 数据子列表
     */
    public static java.util.List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis 读取 List 内容异常: key={}", key, e);
            return null;
        }
    }

    /**
     * 获取 List 的当前长度 (Size)
     *
     * @param key 缓存键名
     * @return 列表长度
     */
    public static Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 裁减 (Trim) 列表，仅保留指定范围内的元素
     *
     * @param key   缓存键名
     * @param start 保留范围起始位
     * @param end   保留范围结束位
     */
    public static void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

}
