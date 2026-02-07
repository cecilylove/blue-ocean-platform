package com.cecilylove.blueocean.web.properties;

import com.cecilylove.blueocean.web.handler.GlobalExceptionHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web 模块统一配置类 (聚合模式)
 */
@Data
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {

    public static final String PREFIX = "blue-ocean.web";

    /**
     * Web 模块总开关
     */
    private boolean enabled = true;

    /**
     * 全局异常处理配置
     */
    private GlobalExceptionHandlerProperties globalExceptionHandler = new GlobalExceptionHandlerProperties();

    /**
     * Jackson 配置
     */
    private JacksonProperties jackson = new JacksonProperties();

    // ================== 分组定义 ==================

    /**
     * 全局异常处理器配置
     */

    @Data
    public static class GlobalExceptionHandlerProperties {

        public static final String PREFIX = WebProperties.PREFIX + ".global-exception-handler";

        /**
         * 是否启用全局异常处理器
         * 默认 true
         */
        private boolean enabled = true;
    }

    /**
     * Jackson 序列化配置
     */
    @Data
    public static class JacksonProperties {

        public static final  String PREFIX = WebProperties.PREFIX +".jackson";

        /**
         * 是否启用 Jackson 配置
         * 默认 true
         */
        private boolean enabled = true;

        /**
         * 是否开启 Long 类型自动转 String（解决前端 JS 精度丢失问题）
         * 默认 false
         */
        private boolean enableLongToString = true;

        /**
         * 是否开启全局日期格式化
         * 默认 false
         */
        private boolean enableDateFormat = false;

        /**
         * 全局日期格式化
         * 默认 yyyy-MM-dd HH:mm:ss
         */
        private String dateFormat = "yyyy-MM-dd HH:mm:ss";

        /**
         * 时区设置
         * 默认 GMT+8
         */
        private String timeZone;
    }
}