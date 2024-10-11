package com.example.redis.provider.stater.multicache.autoconfigure;

import java.util.Map;

import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.example.redis.provider.stater.autoconfigure.RedissonAutoConfiguration;
import com.example.redis.provider.stater.multicache.key.ToStringKeyGenerator;
import com.example.redis.provider.stater.multicache.properties.RedissonCaffeineCacheConfig;
import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheListener;
import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheManager;
import com.example.redis.provider.stater.porperties.RedissonConfigProperties;
import com.google.common.base.Throwables;

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
//redisson客户端加载后进行装配
@ConditionalOnBean(RedissonClient.class)
@AutoConfigureAfter({RedissonAutoConfiguration.class})
public class MultiCacheAutoConfiguration implements ResourceLoaderAware, ApplicationContextAware, CachingConfigurer {

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

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
        Resource resource = resourceLoader.getResource("classpath:cache-config.json");
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

    //--------------------------------------------------------spring cache 配置----------------------------------------------------//

    /**
     * key生成器 如果使用redisson则使用自定义，否则使用原生
     *
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return redissonConfigProperties.isClient() ? applicationContext.getBean(ToStringKeyGenerator.class) : CachingConfigurer.super.keyGenerator();
    }

    /**
     * 设置缓存管理器
     *
     * @return
     */
    @Override
    public CacheManager cacheManager() {
        return redissonConfigProperties.isMultiCache() ? applicationContext.getBean(RedissonCaffeineCacheManager.class) : CachingConfigurer.super.cacheManager();
    }

    /**
     * 缓存数据操作异常处理 这里的处理：在日志中打印出错误信息，但是放行
     * 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得能够出问题时不用缓存
     *
     * @return
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache,
                                            Object key, Object value) {
                cacheError("handleCachePutError", exception, cache.getName(), key);
            }

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache,
                                            Object key) {
                cacheError("handleCacheGetError", exception, cache.getName(), key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache,
                                              Object key) {
                cacheError("handleCacheEvictError", exception, cache.getName(), key);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                cacheError("handleCacheClearError", exception, cache.getName(), null);
            }
        };
    }

    protected void cacheError(String method, Exception exception, String cachename, Object key) {
        log.error("caffeine redisson cache exception, the method is [{}], cache name is [{}], key is [{}], exception is [{}]", method, cachename, key, Throwables.getStackTraceAsString(exception));
    }

    public MultiCacheAutoConfiguration(RedissonConfigProperties redissonConfigProperties, RedissonClient redissonClient) {
        this.redissonConfigProperties = redissonConfigProperties;
        this.redissonClient = redissonClient;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
