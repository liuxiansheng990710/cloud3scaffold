package com.example.redis.provider.stater.lock.handler;

import com.example.redis.provider.stater.lock.model.LockInfo;

/**
 * <p>
 * 解锁超时处理
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 11:06
 */

public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);

}
