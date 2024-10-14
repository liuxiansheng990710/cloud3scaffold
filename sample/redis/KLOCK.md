# 自定义[分布式锁注解](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/lock/annotations/Klock.java)使用方式

## 正常使用

- 配置锁名称（如果不配则已类名+方法名作为锁名称）
- 配置[锁的类型](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/lock/enums/LockType.java)
- 配置锁超时时间（waitTime）及解锁时间（leaseTime）

```java
import com.example.redis.provider.stater.lock.annotations.Klock;
import com.example.redis.provider.stater.lock.annotations.KlockKey;

public class test{
    @Klock(name = "app1", lockType = LockType.WRITE, waitTime = 1, leaseTime = 5)
    public void get(@KlockKey String name) {
        // do something
    }
}
```