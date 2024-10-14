package com.example.redis.provider.stater.lock.handler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.commons.core.exceptions.KlockInvocationException;
import com.example.redis.provider.stater.lock.annotations.Klock;
import com.example.redis.provider.stater.lock.enums.LockType;
import com.example.redis.provider.stater.lock.excutor.locks.LockExcutorFactory;
import com.example.redis.provider.stater.lock.excutor.provider.LockInfoProvider;
import com.example.redis.provider.stater.lock.excutor.strategy.LockTimeoutStrategy;
import com.example.redis.provider.stater.lock.excutor.strategy.ReleaseTimeoutStrategy;
import com.example.redis.provider.stater.lock.model.LockInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Klock注解切面处理器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:27
 */

@Aspect
@Component
@Order(0)
@Slf4j
public class KlockAspectHandler {

    private final Map<String, LockInfo> currentThreadLock = new ConcurrentHashMap<>();

    @Around(value = "@annotation(klock)")
    public Object around(ProceedingJoinPoint joinPoint, Klock klock) throws Throwable {
        LockInfo lockInfo = LockInfoProvider.getLockInfo(joinPoint, klock);
        //存放锁信息
        String curentLock = this.getCurrentLockId(joinPoint, klock);
        currentThreadLock.put(curentLock, lockInfo);
        boolean lock = LockExcutorFactory.acquireLock(lockInfo);
        //获取锁失败 进入失败处理逻辑
        if (!lock) {
            if (log.isWarnEnabled()) {
                log.warn("加锁超时 如果是可重入锁 暂时忽略这条日志：锁key({})", lockInfo.getName());
            }
            if (!klock.customLockTimeoutStrategy().isEmpty()) {
                return LockTimeoutStrategy.handleCustomLockTimeout(klock.customLockTimeoutStrategy(), joinPoint);
            } else {
                //注意：如果没有指定预定义的策略，默认的策略为不做处理 继续执行下面逻辑
                klock.lockTimeoutStrategy().handle(lockInfo, joinPoint);
            }
        }
        currentThreadLock.get(curentLock).setLock(true);
        return joinPoint.proceed();
    }

    @AfterReturning(value = "@annotation(klock)")
    public void afterReturning(JoinPoint joinPoint, Klock klock) {
        String curentLock = this.getCurrentLockId(joinPoint, klock);
        releaseLock(klock, joinPoint, curentLock);
        cleanUpThreadLocal(curentLock);
    }

    @AfterThrowing(value = "@annotation(klock)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Klock klock, Throwable ex) throws Throwable {
        String curentLock = this.getCurrentLockId(joinPoint, klock);
        releaseLock(klock, joinPoint, curentLock);
        cleanUpThreadLocal(curentLock);
        throw ex;
    }

    /**
     * 释放锁
     *
     * @param klock
     * @param joinPoint
     * @param curentLock
     */
    private void releaseLock(Klock klock, JoinPoint joinPoint, String curentLock) {
        LockInfo lockInfo = currentThreadLock.get(curentLock);
        if (Objects.isNull(lockInfo)) {
            throw new KlockInvocationException("请检查是否修改了输入参数 导致锁键不一致：锁键({})" + curentLock);
        }
        if (lockInfo.isLock()) {
            boolean releaseLock = LockExcutorFactory.releaseLock(lockInfo);
            if (!releaseLock) {
                handleReleaseTimeout(klock, lockInfo, joinPoint);
            }
        }
    }

    /**
     * 处理释放锁时超时
     *
     * @param klock
     * @param lockInfo
     * @param joinPoint
     */
    private void handleReleaseTimeout(Klock klock, LockInfo lockInfo, JoinPoint joinPoint) {
        if (log.isWarnEnabled()) {
            log.warn("Timeout while release Lock({})", lockInfo.getName());
        }
        if (!klock.customReleaseTimeoutStrategy().isEmpty()) {
            ReleaseTimeoutStrategy.handleCustomReleaseTimeout(klock.customReleaseTimeoutStrategy(), joinPoint);
        } else {
            //注意：如果没有指定预定义的策略，默认的策略为不做处理 继续执行下面逻辑
            klock.releaseTimeoutStrategy().handle(lockInfo);
        }
    }

    private void cleanUpThreadLocal(String curentLock) {
        LockType lockType = currentThreadLock.get(curentLock).getLockType();
        if (Objects.equals(lockType, LockType.REENTRANT)) {
            if (LockExcutorFactory.getCounter(curentLock).get() == 0) {
                currentThreadLock.remove(curentLock);
                LockExcutorFactory.removeCounter(curentLock);
            }
        } else {
            currentThreadLock.remove(curentLock);
        }
    }

    private String getCurrentLockId(JoinPoint joinPoint, Klock klock) {
        LockInfo lockInfo = LockInfoProvider.getLockInfo(joinPoint, klock);
        return LockExcutorFactory.getCurrentLockId(lockInfo.getName());
    }

}
