package com.example.redis.provider.stater.autoconfigure;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.example.redis.provider.stater.genertor.ToStringKeyGenerator;
import com.example.redis.provider.stater.porperties.RedissonConfigProperties;

import lombok.NonNull;

/**
 * <p>
 * redisson自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 14:54
 */

@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonConfigProperties.class)
@ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".enable", matchIfMissing = true)
public class RedissonAutoConfiguration implements ResourceLoaderAware, ApplicationContextAware, CachingConfigurer {

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    private final RedissonConfigProperties redissonConfigProperties;

    /**
     * redisson 客户端配置
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".client", matchIfMissing = true)
    public RedissonClient redissonClient() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:redisson-config.json");
        ConfigSupport configSupport = new ConfigSupport();
        return Redisson.create(configSupport.fromJSON(resource.getInputStream(), Config.class));
    }

    /**
     * redisson 缓存管理器（仅redis）
     *
     * @param redissonClient
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".manager", matchIfMissing = true)
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient, "classpath:cache-config.json");
    }

    //--------------------------------------------------------spring cache 配置----------------------------------------------------//

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

    /**
     * key生成器 如果使用redisson则使用自定义，否则使用原生
     *
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return redissonConfigProperties.isClient() ? applicationContext.getBean(ToStringKeyGenerator.class) : CachingConfigurer.super.keyGenerator();
    }

    public RedissonAutoConfiguration(RedissonConfigProperties redissonConfigProperties) {
        this.redissonConfigProperties = redissonConfigProperties;
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
