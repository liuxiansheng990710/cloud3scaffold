package com.example.redis.provider.stater.multicache.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 缓存消息，用于节点间通信
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 17:13
 */

@Data
@NoArgsConstructor
public class CacheMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存KEY
     */
    private Object key;

    /**
     * 服务名称
     */
    private String applicationName;

    public CacheMessage(String cacheName, Object key, String applicationName) {
        this.cacheName = cacheName;
        this.key = key;
        this.applicationName = applicationName;
    }

    public CacheMessage(String cacheName, Object key) {
        this.cacheName = cacheName;
        this.key = key;
    }
}
