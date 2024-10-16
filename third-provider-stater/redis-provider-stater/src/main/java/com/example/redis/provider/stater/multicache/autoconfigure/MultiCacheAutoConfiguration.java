package com.example.redis.provider.stater.multicache.autoconfigure;

import java.util.Map;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.example.redis.provider.stater.autoconfigure.RedissonAutoConfiguration;
import com.example.redis.provider.stater.multicache.key.ToStringKeyGenerator;
import com.example.redis.provider.stater.multicache.properties.RedissonCaffeineCacheConfig;
import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheListener;
import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheManager;
import com.example.redis.provider.stater.porperties.RedissonConfigProperties;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 多级缓存自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 17:58
 */

@Slf4j
@Configuration
@AutoConfigureAfter({RedissonAutoConfiguration.class})
public class MultiCacheAutoConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private final RedissonConfigProperties redissonConfigProperties;

    private final RedissonClient redissonClient;

    /**
     * redisson 缓存管理器（支持多级缓存）
     *
     * @return
     */
    @Bean
    @Primary
    @SneakyThrows
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".multi-cache", matchIfMissing = true)
    public RedissonCaffeineCacheManager caffeineCacheManager() {
        Resource resource = resourceLoader.getResource(redissonConfigProperties.getMulti().getConfig());
        Map<String, RedissonCaffeineCacheConfig> cacheConfigMap = RedissonCaffeineCacheConfig.fromJSON(resource.getInputStream());
        return new RedissonCaffeineCacheManager(redissonConfigProperties, redissonClient, cacheConfigMap);
    }

    /**
     * redisson 多级缓存清理监听器
     *
     * @param caffeineCacheManager
     * @return
     */
    @Bean
    @ConditionalOnBean(RedissonCaffeineCacheManager.class)
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".multi-cache", matchIfMissing = true)
    public RedissonCaffeineCacheListener redissonCaffeineCacheListener(RedissonCaffeineCacheManager caffeineCacheManager) {
        return new RedissonCaffeineCacheListener(caffeineCacheManager, redissonClient, redissonConfigProperties.getMulti().getTopic());
    }

    /**
     * key生成器
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".client", matchIfMissing = true)
    public ToStringKeyGenerator toStringKeyGenerator() {
        return new ToStringKeyGenerator();
    }

    @Lazy
    public MultiCacheAutoConfiguration(RedissonConfigProperties redissonConfigProperties, RedissonClient redissonClient) {
        this.redissonConfigProperties = redissonConfigProperties;
        this.redissonClient = redissonClient;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

}
