package com.example.redis.provider.stater.multicache.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import com.example.redis.provider.stater.multicache.properties.RedissonCaffeineCacheConfig;
import com.example.redis.provider.stater.porperties.RedissonConfigProperties;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 构建多级缓存管理器对象
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 16:41
 */

@Slf4j
public class RedissonCaffeineCacheManager extends RedissonSpringCacheManager {

    private final ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    /**
     * 缓存存在过期时间名称集合
     */
    @Getter
    private final Set<String> expireCacheNames = new HashSet<>();

    private final RedissonClient redissonClient;

    private final boolean dynamic;

    private final String topic;

    private final Map<String, RedissonCaffeineCacheConfig> configMap;

    private final RedissonCaffeineCacheConfig caffeineCacheConfig;

    private ApplicationContext applicationContext;

    public RedissonCaffeineCacheManager(RedissonConfigProperties redissonConfigProperties,
                                        RedissonClient redissonClient, Map<String, RedissonCaffeineCacheConfig> configMap) {
        super(redissonClient, configMap, null);
        this.configMap = configMap;
        this.caffeineCacheConfig = redissonConfigProperties.getMulti();
        this.redissonClient = redissonClient;
        this.dynamic = redissonConfigProperties.getMulti().isDynamic();
        this.topic = redissonConfigProperties.getMulti().getTopic();
    }

    /**
     * 创建默认cache 配置
     *
     * @return
     */
    @Override
    protected RedissonCaffeineCacheConfig createDefaultConfig() {
        RedissonCaffeineCacheConfig defaultConfig = new RedissonCaffeineCacheConfig();
        defaultConfig.setTTL(caffeineCacheConfig.getTTL());
        defaultConfig.setMaxIdleTime(caffeineCacheConfig.getMaxIdleTime());
        defaultConfig.setMaxSize(caffeineCacheConfig.getMaxSize());
        defaultConfig.setVagueEvict(caffeineCacheConfig.isVagueEvict());
        defaultConfig.setMultiLevelCache(caffeineCacheConfig.isMultiLevelCache());
        return defaultConfig;
    }

    /**
     * 获取缓存对象
     *
     * @param name 缓存名称
     * @return
     */
    @Override
    public Cache getCache(String name) {
        RedissonCaffeineCacheConfig config = configMap.get(name);
        if (Objects.isNull(config)) {
            config = createDefaultConfig();
            configMap.put(name, config);
        }
        //没开多级缓存 直接使用父类构建缓存对象方法
        if (!config.isMultiLevelCache()) {
            return super.getCache(name);
        }
        //如果已经存在cache对象，直接返回，无需构建
        Cache cache = instanceMap.get(name);
        if (Objects.nonNull(cache)) {
            return cache;
        }
        //提前该步骤 节省时间：没有开启动态创建 且 没有该缓存时 直接返回null 不往下走
        if (!dynamic && !super.getCacheNames().contains(name)) {
            return null;
        }
        if (config.getTTL() == 0 && config.getMaxIdleTime() == 0 && config.getMaxSize() == 0) {
            //永久存储时，使用RMap
            RMap<Object, Object> map = super.getMap(name, config);
            cache = new RedissonCaffeineCache(applicationContext, redissonClient, map, createCaffeineCache(config), topic,
                    config.isAllowNullValues(), config.isVagueEvict(), config.isClone());
        } else {
            //存在过期时间时，使用RMapCache
            RMapCache<Object, Object> map = super.getMapCache(name, config);
            cache = new RedissonCaffeineCache(applicationContext, redissonClient, map, config, createCaffeineCache(config), topic,
                    config.isAllowNullValues(), config.isVagueEvict(), config.isClone());
            expireCacheNames.add(name);
        }
        //将cache对象放入Map 防止多线程时多次存入，如果有值 直接返回旧值，不存在则返回null
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (log.isDebugEnabled()) {
            log.debug("create caffeine redisson cache instance, the cache name is [{}]", name);
        }
        return Objects.isNull(oldCache) ? cache : oldCache;
    }

    /**
     * 构建一级缓存
     *
     * @param cacheConfig
     * @return
     */
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> createCaffeineCache(CacheConfig cacheConfig) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (cacheConfig.getMaxIdleTime() > 0) {
            //访问后到期，从上次读或写发生后的过期时间
            cacheBuilder.expireAfterAccess(cacheConfig.getMaxIdleTime(), TimeUnit.MILLISECONDS);
        }
        if (cacheConfig.getTTL() > 0) {
            //写入后到期，从上次写入发生之后的过期时间
            cacheBuilder.expireAfterWrite(cacheConfig.getTTL(), TimeUnit.MILLISECONDS);
        }
        if (caffeineCacheConfig.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(caffeineCacheConfig.getInitialCapacity());
        }
        if (caffeineCacheConfig.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(caffeineCacheConfig.getMaximumSize());
        }
        return cacheBuilder.softValues().build();
    }

    /**
     * 缓存清理
     *
     * @param cacheName
     * @param key
     */
    public boolean clearCaffeineCache(String cacheName, Object key) {
        Cache cache = instanceMap.get(cacheName);
        if (cache instanceof RedissonCaffeineCache redisCaffeineCache) {
            redisCaffeineCache.clearCaffeineCache(key);
            return true;
        }
        return false;
    }

}