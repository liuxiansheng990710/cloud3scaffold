package com.example.provider.base.model;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mysql.provider.stater.mp.service.BaseService;
import com.example.mysql.provider.stater.mp.service.impl.BaseServiceImpl;
import com.example.redis.provider.stater.lock.annotations.KlockKey;

@Service
public class AppServices extends BaseServiceImpl<AppMapper, App> implements BaseService<App> {

    @Cacheable(value = "app1")
//    @Klock(name = "app1", lockType = LockType.WRITE, waitTime = 1, leaseTime = 5, error = "我错了", lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    public App get(@KlockKey String name) {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return getOne(Wrappers.<App>lambdaQuery().eq(App::getName, name));
    }

    public void evict() {

    }

}
