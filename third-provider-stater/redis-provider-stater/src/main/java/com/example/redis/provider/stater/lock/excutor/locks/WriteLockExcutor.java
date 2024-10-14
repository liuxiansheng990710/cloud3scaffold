package com.example.redis.provider.stater.lock.excutor.locks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.redis.provider.stater.lock.enums.LockType;
import com.example.redis.provider.stater.lock.model.LockInfo;

/**
 * <p>
 * 写锁执行器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:23
 */

@Component
public class WriteLockExcutor implements LockExcutor {

    @Autowired
    private RedissonClient redissonClient;

    private RLock writeLock;

    @Override
    public boolean acquireLock(LockInfo lockInfo) {
        writeLock = redissonClient.getReadWriteLock(lockInfo.getName()).writeLock();
        try {
            return writeLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean releaseLock(LockInfo lockInfo) {
        if (writeLock.isHeldByCurrentThread()) {
            try {
                return writeLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    @Override
    public LockType lockType() {
        return LockType.WRITE;
    }
}
