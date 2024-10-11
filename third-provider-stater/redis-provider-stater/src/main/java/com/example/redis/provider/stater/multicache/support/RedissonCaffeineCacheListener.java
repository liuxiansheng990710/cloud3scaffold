package com.example.redis.provider.stater.multicache.support;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.MapCacheEventCodec;
import org.redisson.codec.SerializationCodec;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.core.Ordered;

import com.example.redis.provider.stater.multicache.model.CacheMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 缓存监听器 主要作用是清理分布式情况下的一级缓存
 * </p>
 *
 * @author : 21
 * @since : 2024/10/11 10:40
 */

@Slf4j
public record RedissonCaffeineCacheListener(
        RedissonCaffeineCacheManager cacheManager,
        RedissonClient redissonClient, String topic) implements ApplicationRunner, Ordered {

    //由于实现ApplicationRunner，所以在应用启动后就注册监听器，会一直挂在应用中
    @Override
    public void run(ApplicationArguments args) {
        //清理一级缓存,因为这里是put，clear，envit方法才会触发，所以只需要关注一级缓存的清理就行
        RTopic topic = redissonClient.getTopic(this.topic, new SerializationCodec());
        //清理因触发导致失效的一级缓存
        topic.addListenerAsync(CacheMessage.class, ((channel, msg) -> {
            boolean isClear = cacheManager.clearCaffeineCache(msg.getCacheName(), msg.getKey());
            if (isClear && log.isDebugEnabled()) {
                log.debug("caffeine cache clear finished, msg is [{}]", msg);
            }
        }));
        //清理过期的一级缓存（这里主要是在服务首次启动时，清理其他节点过期，但是本节点由于不在线没有收到topic 而导致未清理的缓存）
        cacheManager.getExpireCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof RedissonCaffeineCache redissonCaffeineCache) {
                com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = redissonCaffeineCache.getCaffeineCache();
                RMapCache<Object, Object> mapCache = redissonClient.getMapCache(cacheName);
                //在监听中清理一级缓存
                EntryExpiredListener<Object, Object> listener = getEntryExpiredListener(cacheName, redissonCaffeineCache, caffeineCache);
                RTopic entryExpiredTopic = redissonClient.getTopic(getExpiredChannelName(mapCache.getName()), new MapCacheEventCodec(JsonJacksonCodec.INSTANCE, null));
                entryExpiredTopic.addListener(List.class, (MessageListener<List<Object>>) (channel, msg) -> {
                    EntryEvent<Object, Object> event = new EntryEvent<>(mapCache, EntryEvent.Type.EXPIRED, msg.get(0), msg.get(1), null);
                    //执行监听器他自己的逻辑
                    listener.onExpired(event);
                });
            }
        });
    }

    /**
     * 获取过期缓存监听
     *
     * @param cacheName
     * @param redissonCaffeineCache
     * @param caffeineCache
     * @return
     */
    private EntryExpiredListener<Object, Object> getEntryExpiredListener(String cacheName, RedissonCaffeineCache redissonCaffeineCache, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache) {
        return event -> entryExpired(cacheName, redissonCaffeineCache, caffeineCache, event);
    }

    /**
     * 具体一级缓存清理逻辑
     *
     * @param cacheName
     * @param redissonCaffeineCache
     * @param caffeineCache
     * @param event
     */
    private void entryExpired(String cacheName, RedissonCaffeineCache redissonCaffeineCache, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache, EntryEvent<Object, Object> event) {
        //这里的event是redisson过期监听器，从这里取过期缓存
        Object key = event.getKey();
        // 缓存过期后先清理本地的一级缓存
        caffeineCache.invalidate(key);
        // 获取分布式锁
        RLock lock = redissonClient.getLock(cacheName + "_" + key);
        try {
            boolean tryLock = lock.tryLock(0L, 1000L, TimeUnit.MILLISECONDS);
            if (tryLock) {
                // 拿到锁的节点发送清理全局消息（清理兄弟节点的一级缓存）
                redissonCaffeineCache.publish(key, "entryExpired");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            //异步解锁
            if (lock.isHeldByCurrentThread()) {
                lock.forceUnlockAsync();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("caffeine cache entryExpired finished, cacheName is [{}], key is [{}]", cacheName, key);
        }
    }

    String getExpiredChannelName(String rawName) {
        return prefixName("redisson_map_cache_expired", rawName);
    }

    public String prefixName(String prefix, String name) {
        if (name.contains("{")) {
            return prefix + ":" + name;
        }
        return prefix + ":{" + name + "}";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}