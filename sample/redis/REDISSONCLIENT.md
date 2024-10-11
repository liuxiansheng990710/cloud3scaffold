# 基于Json配置Redisson客户端

## [Redisson客户端配置](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/autoconfigure/RedissonAutoConfiguration.java)

- 这里的[classpath:redisson-config.json](#config)是放在服务的resources下即可
- 推荐根据不同环境，定义不同json，例如：classpath:redisson-alpha-config.json

//TODO
这里如果有时间，会进行区分环境进行编写


- 如果不想使用该客户端，可参考[redisson自定义配置](../../third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/porperties/RedissonConfigProperties.java)
  在yaml中进行关闭

## config

```json
{
  "singleServerConfig": {
    "idleConnectionTimeout": 10000,
    "connectTimeout": 5000,
    "timeout": 6000,
    "pingConnectionInterval": 3000,
    "retryAttempts": 0,
    "subscriptionsPerConnection": 5,
    "clientName": null,
    "address": "redis://localhost:6379",
    "password": "password",
    "subscriptionConnectionMinimumIdleSize": 1,
    "subscriptionConnectionPoolSize": 25,
    "connectionMinimumIdleSize": 1,
    "connectionPoolSize": 10,
    "database": 1,
    "dnsMonitoringInterval": 5000
  },
  "threads": 5,
  "nettyThreads": 10,
  "codec": {
    "class": "org.redisson.codec.JsonJacksonCodec"
  },
  "transportMode": "NIO"
}
```