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
 * 读锁执行器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:22
*/


@Component
public class ReadLockExcutor implements LockExcutor {

    @Autowired
    private RedissonClient redissonClient;

    private RLock readLock;

    @Override
    public boolean acquireLock(LockInfo lockInfo) {
        readLock = redissonClient.getReadWriteLock(lockInfo.getName()).readLock();
        try {
            return readLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean releaseLock(LockInfo lockInfo) {
        if (readLock.isHeldByCurrentThread()) {
            try {
                return readLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    @Override
    public LockType lockType() {
        return LockType.READ;
    }
}
