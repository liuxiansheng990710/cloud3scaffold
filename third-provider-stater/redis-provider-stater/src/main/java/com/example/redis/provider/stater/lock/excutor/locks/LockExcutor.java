package com.example.redis.provider.stater.lock.excutor.locks;

import com.example.redis.provider.stater.lock.enums.LockType;
import com.example.redis.provider.stater.lock.model.LockInfo;

/**
 * <p>
 * 锁执行器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:21
 */

public interface LockExcutor {

    /**
     * 获取锁
     *
     * @return
     */
    boolean acquireLock(LockInfo lockInfo);

    /**
     * 释放锁
     *
     * @return
     */
    boolean releaseLock(LockInfo lockInfo);

    /**
     * 锁类型
     *
     * @return
     */
    LockType lockType();

}
