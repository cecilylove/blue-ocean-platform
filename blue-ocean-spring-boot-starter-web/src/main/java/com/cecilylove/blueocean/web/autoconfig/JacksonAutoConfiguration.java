package com.cecilylove.blueocean.web.autoconfig;


import com.cecilylove.blueocean.core.constant.GlobalConfigConstants;
import com.cecilylove.blueocean.web.properties.WebProperties;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Jackson 配置自动装配
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = WebProperties.JacksonProperties.PREFIX, name = GlobalConfigConstants.ENABLED, havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class JacksonAutoConfiguration {

    private final WebProperties webProperties;

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        WebProperties.JacksonProperties jacksonProperties = webProperties.getJackson();
        return builder -> {
            // 处理日期格式
            if (jacksonProperties.isEnableDateFormat()) {
                String pattern = jacksonProperties.getDateFormat();
                if (pattern != null && !pattern.isEmpty()) {
                    builder.simpleDateFormat(pattern);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
                    builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
                }
            }

            // 时区
            if (jacksonProperties.getTimeZone() != null) {
                builder.timeZone(TimeZone.getTimeZone(jacksonProperties.getTimeZone()));
            }

            // 处理 Long 转 String
            if (jacksonProperties.isEnableLongToString()) {
                builder.serializerByType(Long.class, ToStringSerializer.instance);
                builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
                builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            }
        };
    }
}