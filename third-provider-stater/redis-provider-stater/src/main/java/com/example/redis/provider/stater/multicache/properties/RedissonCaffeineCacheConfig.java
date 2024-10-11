package com.example.redis.provider.stater.multicache.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.redisson.spring.cache.CacheConfig;

import com.example.redis.provider.stater.multicache.support.RedissonCaffeineCacheConfigSupport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 多级缓存配置
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 16:35
 */

@Getter
@Setter
@NoArgsConstructor
public class RedissonCaffeineCacheConfig extends CacheConfig {

    public RedissonCaffeineCacheConfig(long ttl, long maxIdleTime) {
        super(ttl, maxIdleTime);
    }

    /**
     * 缓存更新时通知其他节点的topic名称
     */
    private String topic = "cache:caffeine:redisson:topic";

    /**
     * 当前缓存使用时 是否启用多级缓存
     */
    private boolean multiLevelCache = true;

    /**
     * 是否根据cacheName动态创建Cache 与 SpringCache中dynamic保持一致
     * 解释：根据cacheName获取缓存 缓存不存在时 -> 查询数据库 -> 并设置到该cacheName中 -> 下次调用时存在直接返回
     */
    private boolean dynamic = true;

    /**
     * 是否支持模糊清理，当缓存KEY为String并且KEY中包含'*'，以及该字段设置为true时模糊清理才会生效
     */
    private boolean vagueEvict = false;

    /**
     * 是否允许缓存null值（作用于缓存穿透）
     */
    private boolean allowNullValues = true;

    /**
     * 是否开启克隆（获取到克隆后的缓存对象 不直接操作原缓存对象）
     * 只针对一级缓存
     */
    private boolean clone = false;

    /**
     * 一级缓存初始化大小
     */
    private int initialCapacity = 20;

    /**
     * 最大缓存对象个数，超过此数量时之前放入的缓存将失效
     */
    private long maximumSize = 1000;

    /**
     * 因暂用该方法 所以只提取该方法，若需要可自行提取
     * {@link org.redisson.spring.cache.CacheConfig}
     */
    public static Map<String, RedissonCaffeineCacheConfig> fromJSON(InputStream inputStream) throws IOException {
        return new RedissonCaffeineCacheConfigSupport().fromJSON(inputStream);
    }
}
