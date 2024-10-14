package com.example.redis.provider.stater.porperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.example.redis.provider.stater.lock.properties.KlockConfig;
import com.example.redis.provider.stater.multicache.properties.RedissonCaffeineCacheConfig;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES)
public class RedissonConfigProperties {

    public static final String REDISSON_CONFIG_PROPERTIES = "redisson.custom";

    /**
     * 是否启用redisson自定义配置
     */
    private boolean enable = true;

    /**
     * 是否启用自定义redisson客户端
     */
    private boolean client = true;

    /**
     * 是否全局启用多级缓存
     */
    private boolean multiCache = true;

    /**
     * 多级缓存配置，默认缓存十五分钟
     */
    @NestedConfigurationProperty
    private RedissonCaffeineCacheConfig multi = new RedissonCaffeineCacheConfig(900000L, 900000L);

    /**
     * 分布式锁配置
     */
    @NestedConfigurationProperty
    private KlockConfig klock = new KlockConfig();

}
