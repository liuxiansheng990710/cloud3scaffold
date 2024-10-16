package com.example.redis.provider.stater.autoconfigure;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.example.redis.provider.stater.porperties.RedissonConfigProperties;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * redisson自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 14:54
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonConfigProperties.class)
@ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".enable", matchIfMissing = true)
public class RedissonAutoConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    /**
     * redisson 客户端配置
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(name = RedissonConfigProperties.REDISSON_CONFIG_PROPERTIES + ".client", matchIfMissing = true)
    public RedissonClient redissonClient(RedissonConfigProperties redissonConfigProperties) throws IOException {
        Resource resource = resourceLoader.getResource(redissonConfigProperties.getConfig());
        ConfigSupport configSupport = new ConfigSupport();
        return Redisson.create(configSupport.fromJSON(resource.getInputStream(), Config.class));
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
