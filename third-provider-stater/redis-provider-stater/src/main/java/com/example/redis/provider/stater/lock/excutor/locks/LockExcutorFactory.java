package com.example.redis.provider.stater.lock.excutor.locks;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.commons.core.exceptions.KlockInvocationException;
import com.example.redis.provider.stater.lock.enums.LockType;
import com.example.redis.provider.stater.lock.model.LockInfo;

/**
 * <p>
 * 锁执行工厂
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:22
 */

@Component
public class LockExcutorFactory implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private static EnumMap<LockType, LockExcutor> excutorEnumMap;

    /**
     * 可重入锁计数器
     */
    private static final Map<String, AtomicInteger> REENTRANT_LOCK_COUNTER = new ConcurrentHashMap<>();

    /**
     * 获取锁
     *
     * @param lockInfo
     * @return
     */
    public static boolean acquireLock(LockInfo lockInfo) {
        LockExcutor lockExcutor = excutorEnumMap.get(lockInfo.getLockType());
        if (Objects.nonNull(lockExcutor)) {
            return lockExcutor.acquireLock(lockInfo);
        }
        throw new KlockInvocationException("this lock type excutor is not exist");
    }

    /**
     * 释放锁
     *
     * @param lockInfo
     * @return
     */
    public static boolean releaseLock(LockInfo lockInfo) {
        LockExcutor lockExcutor = excutorEnumMap.get(lockInfo.getLockType());
        if (Objects.nonNull(lockExcutor)) {
            return lockExcutor.releaseLock(lockInfo);
        }
        throw new KlockInvocationException("this lock type excutor is not exist");
    }

    /**
     * 获取当前线程锁id
     *
     * @param lockKeyName
     * @return
     */
    public static String getCurrentLockId(String lockKeyName) {
        return Thread.currentThread().getId() + lockKeyName;
    }

    /**
     * 获取锁计数器(只有可重入锁可用)
     *
     * @param lockId
     */
    public static AtomicInteger getCounter(String lockId) {
        return REENTRANT_LOCK_COUNTER.get(lockId);
    }

    /**
     * 初始化锁计数器值(只有可重入锁可用)
     *
     * @param lockId
     */
    public static void initCounter(String lockId) {
        REENTRANT_LOCK_COUNTER.put(lockId, new AtomicInteger());
    }

    /**
     * 删除锁计数器(只有可重入锁可用)
     *
     * @param lockId
     */
    public static void removeCounter(String lockId) {
        REENTRANT_LOCK_COUNTER.remove(lockId);
    }

    /**
     * 获取锁的状态
     *
     * @param lock
     * @param lockId
     * @return false 解锁；true 加锁中
     */
    public static boolean getState(Lock lock, String lockId) {
        if (lock instanceof ReentrantLock) {
            //保证可重入锁，解锁次数,用于计数
            AtomicInteger reentrantLockCount = getCounter(lockId);
            return Objects.nonNull(reentrantLockCount) && reentrantLockCount.get() != 0;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, LockExcutor> beansOfType = applicationContext.getBeansOfType(LockExcutor.class);
        if (CollectionUtils.isEmpty(beansOfType)) {
            return;
        }
        excutorEnumMap = new EnumMap<>(LockType.class);
        beansOfType.forEach((containerNodeBeanName, containerNodeService) -> {
            LockType lockType = containerNodeService.lockType();
            if (Objects.nonNull(lockType)) {
                excutorEnumMap.put(lockType, containerNodeService);
            }
        });

    }
}
