package com.cecilylove.blueocean.framework.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * Spring 上下文工具类
 * <p>
 * 用于在普通 Java 类（非 Bean 容器管理类）中手动获取 Spring Bean 或发布应用事件。
 * 实现了 {@link ApplicationContextAware} 接口，由 Spring 容器在启动阶段自动注入上下文。
 *
 * @author cecilylove
 * @since 1.0.0
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取 ApplicationContext 实例
     *
     * @return 当前 Spring 应用上下文
     * @throws IllegalStateException 若上下文尚未由容器注入（通常是因为调用过早）
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 通过 Bean 名称获取实例
     *
     * @param <T>  目标泛型类型
     * @param name Bean 在容器中的注册名称
     * @return Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过 Class 类型获取 Bean 实例
     *
     * @param <T>   目标泛型类型
     * @param clazz Bean 的 Class 类型
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过名称与 Class 类型双向匹配获取 Bean
     *
     * @param <T>   目标泛型类型
     * @param name  Bean 名称
     * @param clazz Bean 的 Class 类型
     * @return Bean 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 向 Spring 容器发布事件
     *
     * @param event 应用事件对象
     */
    public static void publishEvent(ApplicationEvent event) {
        if (applicationContext != null) {
            applicationContext.publishEvent(event);
        }
    }

    /**
     * 校验上下文注入状态，防止 NPE 及因启动顺序导致的隐晦错误
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("SpringUtil 的 applicationContext 为空。请确保 SpringUtil 已注入容器并作为 Bean 加载。此外，请检查调用时机是否过早（早于上下文初始化完成点）。");
        }
    }
}