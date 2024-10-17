# 微服务脚手架

## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 3.3.4 、Mybatis-Plus 3.5.7 **微服务架构**
- 提供 lambda 、stream api 、webflux 的生产实践

## 快速开始

### 说明

- 1.0.0 : jdk17 + springboot 3.3.4 + springcloud 2021

| 分支   | 说明                                             |
| ------ | ------------------------------------------------ |
| master | jdk17 + springboot 3.3.4 + springcloud 2021.0.5 + mybatis-plus 3.5.7 |

### 核心依赖

| 依赖                        | 版本       |
| --------------------------- | ---------- |
| gradle  | 8.8        |
| Spring Boot                 | 3.3.4      |
| MyBatis Plus                | 3.5.7      |

### 支持

- [x] 日志（链路追踪，请求日志，mongo，mySql，pgSql，mq，okhttp3）
- [x] mongo（自定义id、操作人信息、操作时间等自动填充，mongo日志打印，时间类型精确转换）
- [x] mysql（mybatis-plus，日志打印，公共方法封装，自动填充，数据库字段自动校验）
- [x] redis（redisson，分布式锁，分布式下多级缓存，spring cache）
- [x] 定时任务（quartz，可视化，页面型操作，运行日志，自动注册，异步执行）

### 模块说明

```
cloud3scaffold
└── commons -- 系统公共模块
     ├── commons-web -- web配置包
     ├── commons-core -- 公共配置/依赖包
└── third-provder-stater -- 第三方支持模块
     ├── mongo-provider-stater -- mongo支持配置
     ├── mysql-provider-stater -- mysql支持配置
     ├── redis-provider-stater -- redis支持配置
     ├── quartz-provider-stater -- 定时任务支持配置
```

### 问题解决 请查看[帮助文档](./HELP.md)

### 项目描述 请查看[相关描述](./DESC.md)
