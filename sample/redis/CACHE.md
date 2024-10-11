# 多级缓存使用示例

## 基于json方式配置Spring Cache缓存管理器

- 在自定义[缓存管理器](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/autoconfigure/MultiCacheAutoConfiguration.java)
的配置中，会使用到[cache-config.json](#config)进行各个缓存的配置，需要放在服务的resources下即可 
  - 主要的作用是对每个缓存的过期时间，最大空闲时间等进行配置，根据对应的配置生成一个缓存管理器，管理缓存生命周期
  - 例如[示例](#config)的配置，就是标识`app1`过期时间为60000ms，最大空闲时间为60000ms

- 在多级缓存开启时，一级、二级缓存的过期时间，最大空闲时间等，均根据该[配置](#config)的值进行设置
  - 更多配置见[缓存配置文件](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/properties/RedissonCaffeineCacheConfig.java)

- 多级缓存管理器实现详见[管理器实现](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/support/RedissonCaffeineCacheManager.java)

## 多级缓存配置

- 默认开启多级缓存，如需关闭详见[全局关闭多级缓存](#全局关闭多级缓存)、[特定缓存关闭多级缓存](#特定缓存关闭多级缓存)
- 多级缓存实现详见[多级缓存实现](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/support/RedissonCaffeineCache.java)

## 分布式下清理一级缓存

- 配置其他节点：当前节点清理一级缓存时，需要一起清理缓存的节点
  - 这里推荐使用服务名进行配置，这样方便
  - 配置示例
    ```yaml
    redisson:
      custom:
        multi:
          topic: ${spring.application.name}:cache:caffeine:redisson:topic
    ```

- 主要分为两部分缓存的生产
  - 由put，clear，envit方法导致当前节点缓存失效，需要清理其他节点的缓存的情况
  - 当前节点宕机，其他节点清理过期缓存时，但是本节点由于不在线没有收到topic，而导致未清理的缓存
- 具体实现逻辑见[缓存监听器](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/support/RedissonCaffeineCacheListener.java)

## 示例

### config

```json
{
  "app1": {
    "ttl": 60000,
    "maxIdleTime": 60000
  }
}
```

### 全局关闭多级缓存

> 在yaml中增加该配置则全局关闭

```yaml
redisson:
  custom:
    multi-cache: true
```

### 特定缓存关闭多级缓存

> 在cache-config.json中，添加multiLevelCache字段的配置，则关闭特定缓存的二级缓存

- 下面示例标识，app1在缓存时，不进行一级缓存
- 如果有特别大的数据，不建议进行一级缓存

```json
{
  "app1": {
    "ttl": 60000,
    "maxIdleTime": 60000,
    "multiLevelCache": false
  }
}
```