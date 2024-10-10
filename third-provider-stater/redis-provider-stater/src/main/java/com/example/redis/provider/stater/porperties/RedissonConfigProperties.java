package com.example.redis.provider.stater.porperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES)
public class RedissonConfigProperties {

    public static final String REDISSON_CONFIG_PROPERTIES = "redis.redisson.custom";

    /**
     * 是否启用redisson自定义配置
     */
    private boolean enable = true;

    /**
     * 是否启用自定义redisson客户端
     */
    private boolean client = true;

    /**
     * 是否启用自定义redisson缓存管理器
     */
    private boolean manager = true;

}
