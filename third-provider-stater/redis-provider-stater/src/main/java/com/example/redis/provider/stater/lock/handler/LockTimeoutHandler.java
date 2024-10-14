package com.example.redis.provider.stater.lock.handler;

import org.aspectj.lang.JoinPoint;

import com.example.redis.provider.stater.lock.model.LockInfo;

/**
 * <p>
 * 加锁超时处理
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:28
 */

public interface LockTimeoutHandler {

    void handle(LockInfo lockInfo, JoinPoint joinPoint);

}
