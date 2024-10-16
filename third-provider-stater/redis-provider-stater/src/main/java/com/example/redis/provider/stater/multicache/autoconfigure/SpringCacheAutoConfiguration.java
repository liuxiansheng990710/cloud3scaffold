package com.example.redis.provider.stater.multicache.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;

import com.example.redis.provider.stater.multicache.key.ToStringKeyGenerator;
import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheManager;
import com.example.redis.provider.stater.porperties.RedissonConfigProperties;
import com.google.common.base.Throwables;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * springCache配置<br>
 * <ol>
 * <li>这里很疑惑，会告警,该bean提前注册，但是不会影响使用</li>
 * <li>不开启这个配置，也是可以正常使用的，所以先不开了吧</li>
 * <li>哪个有缘人遇到这个彩蛋再说吧</li>
 * </ol>
 * </p>
 *
 * @author : 21
 * @since : 2024/10/16 14:53
 */

@Slf4j
@Lazy
//@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(value = {MultiCacheAutoConfiguration.class, AbstractCachingConfiguration.class})
public class SpringCacheAutoConfiguration implements CachingConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final RedissonConfigProperties redissonConfigProperties;

    @Lazy
    public SpringCacheAutoConfiguration(RedissonConfigProperties redissonConfigProperties) {
        this.redissonConfigProperties = redissonConfigProperties;
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

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
