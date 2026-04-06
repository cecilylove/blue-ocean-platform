package com.cecilylove.blueocean.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 基于 Redisson 提供的分布式锁能力，支持可重入锁。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Target(ElementType.METHOD)

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁的Key (支持 SpEL 表达式 如 #user.id)
     */
    String key();

    /**
     * 等待时长 (单位：秒)
     */
    long waitTime() default 10L;

    /**
     * 锁持有时间 (单位：秒，执行完会自动释放，防止死锁的分底线)
     */
    long leaseTime() default 30L;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 是否公平锁
     */
    boolean fair() default false;

}
