# 目录

- [项目说明](#项目说明)
- [gradle](#gradle)
    - [统一依赖管理](#统一依赖管理)
- [web配置](#web)
    - [undertow](#undertow)
    - [mvc序列化与反序列化全局配置](./commons/commons-web/src/main/java/com/example/commons/web/autoconfigure/WebAutoConfiguration.java)
    - [请求参数转换及绑定](./commons/commons-web/src/main/java/com/example/commons/web/servlet/model/SuperController.java)
    - [全局响应处理](#全局响应处理)
    - [全局异常处理](#全局异常处理)
    - [web日志全局配置](#web日志全局配置)
- [mongo配置](#mongo)
    - [id自动填充](#id自动填充)
    - [操作人信息自动填充](#操作人信息自动填充)
    - [时间类型转换](#时间类型转换)
    - [日志配置](#日志配置)
    - [公共方法封装](#公共方法封装)
- [mysql配置](#mysql)
    - [日志打印](#日志配置)
    - [数据库检查](#数据库检查)
    - [mp公共方法封装](#公共方法封装)
    - [mp拦截器](#拦截器)
    - [mp自动填充](#自动填充)
    - [mp自增Id](#id自增)
- [redis配置](#redis)
    - [redisson配置](#redisson配置)
    - [多级缓存](#多级缓存)
    - [分布式锁](#分布式锁)
- [定时任务配置](#定时任务)
    - [自动注册](#自动注册定时任务)
    - [集群配置](#集群配置)
    - [可视化配置](#具体实现)

# 项目说明

<details open>
<summary> 点击展开/折叠 项目说明 </summary>

```
cloud3scaffold
└── commons -- 系统公共模块
     ├── commons-core -- 公共配置/依赖包
            ├── exceptions -- 公共异常
            ├── model -- 公共实体包
            ├── factory -- 分页工厂
            ├── utils -- 工具类
                  ├── config -- 特定工具类配置
            ├── resources/log4j2-local.xml -- 全局日志输出配置
     ├── commons-web -- web配置模块
            ├── autoconfigure -- web自动装配
            ├── servlet -- web servlet配置
                  ├── resolver -- web异常全局处理（包括异常日志）
                  ├── response -- web响应全局处理（包括请求日志）
                  ├── undertow -- undertow配置 
                  ├── interceptors -- 拦截器配置 
                  ├── model -- 公共实体
            ├── runner -- 启动日志配置类
            ├── utils -- 工具类 

└── third-provder-stater -- 第三方支持模块
     ├── mongo-provider-stater -- mongo支持配置
            ├── autoconfigure -- mongo自动装配
            ├── config -- mongo配置（类型转换/自动填充）
            ├── convert -- mongo分页转换
            ├── metrics -- mongo监控
            ├── properties -- mongo自定义配置
            ├── service/model -- mongo公共方法封装
     ├── mysql-provider-stater -- mysql支持配置
            ├── mp -- mybatis-plus自定义配置
                  ├── autoconfigure -- mp自动装配
                  ├── convert -- mp分页转换
                  ├── model -- 公共实体
                  ├── service -- 公共方法
            ├── p6spy -- mysql日志打印
     ├── redis-provider-stater -- redis支持配置
            ├── autoconfigure -- redis自动装配
            ├── multicache -- 多级缓存
                  ├── autoconfigure -- 多级缓存自动装配
                  ├── properties -- 多级缓存自定义配置
                  ├── support -- 多级缓存支持
            ├── properties -- redisson自定义配置
     ├── quartz-provider-stater -- 定时任务支持配置
            ├── autoconfigure -- 集群自动装配
            ├── enums -- 定时任务枚举
            ├── factory -- 定时任务工厂
            ├── support -- 定时任务实现支持   
```

</details>

# gradle

<details open>
<summary> 点击展开/折叠 gradle相关项目描述 </summary>

## 统一依赖管理

基于 [gradle catalog](https://docs.gradle.org/current/userguide/platforms.html) 中的TOML方式实现,
本项目使用groovy方式实现，如需替换为kotlin可参考[gradle kotlin 依赖管理](https://blog.csdn.net/qq_36279799/article/details/131983818)

- [x] gradle 7 之后推荐该用法
- [x] 简化 build.gradle 文件
- [x] 父模块定义dependencies 规范 子模块共享依赖版本

</details>

# web

<details open>
<summary> 点击展开/折叠 web相关项目描述 </summary>

## [undertow](./commons/commons-web/src/main/java/com/example/commons/web/servlet/undertow/UndertowServerFactoryCustomizer.java)

## [全局响应处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/CommonsControllerAdvice.java)

- 响应状态码与http状态码一致
- 如需修改请对@RestControllerAdvice()包路径进行修改
- [Wrapper注解](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/Wrapper.java)
  辅助使用（仅限于类方法上使用）
    - 自定义返回类型：如果不需要对返回结果进行[统一包装](./commons/commons-core/src/main/java/com/example/commons/core/model/Responses.java)处理
      可添加该注解，在返回时则不进行二次包装
    - 自定义返回http状态码：可对httpStatus字段赋值
  > [示例](./sample/response/RESPONSE.md)

- String类型返回值
    - 请使用[统一返回工具类](./commons/commons-web/src/main/java/com/example/commons/web/utils/ResponseUtils.java)中的success(String
      object)方法进行返回
  > [示例](./sample/response/RESPONSE.md)

## [全局异常处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/ServerHandlerExceptionResolver.java)

## web日志全局配置

> [使用示例](./sample/log/LOG.md)

- [正常请求日志](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/CommonsControllerAdvice.java)
- [错误请求日志](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/GlobalExceptionHandler.java)

</details>

# mongo

<details open>
<summary> 点击展开/折叠 mongo相关项目描述 </summary>

## [自定义配置](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/properties/MongoConfigProperties.java)

> 默认开启下面五个配置，如需关闭，请查看[关闭示例](./sample/mongo/PRO.md)

- 注意：自动填充时需要在启动类上添加 **@EnableMongoAuditing** 注解

### [id自动填充](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/config/MongoCompositeKeyFillCallback.java)

> 使用示例详见[示例](./sample/mongo/AUTO.md)

### [操作人信息自动填充](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/config/MongoOperatorAuditorAware.java)

> 使用示例详见[示例](./sample/mongo/AUTO.md)

### [时间类型转换](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/config/DateToMongoDateConvert.java)

### [日志配置](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/metrics/MongoMetricsListener.java)

### [公共方法封装](./third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/service)

> 使用示例详见[示例](./sample/mongo/PUBLIC.md)

</details>

# mysql

<details open>
<summary> 点击展开/折叠 mysql相关项目描述 </summary>

> 默认开启下面四个配置，如需关闭，请查看关闭示例

```yaml
# 全部关闭
mybatis-plus:
  custom:
    enable: false
```

```yaml
# 关闭某个（已自动填充为例）
batis-plus:
  custom:
    auditorAware: false
```

## 日志配置

- 基于 [**p6spy**](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/p6spy) 实现
  使用时详见[配置示例](./sample/mysql/CONFIG.md)

## 数据库检查

- 基于mybaits-plus实现，支持Mysql、PGsql，详见[数据库字段检查](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/injector/ColumnsCheckInjector.java)
- 主要作用：检查代码字段与数据库字段是否一致，防止使用代码字段操作数据库字段时出现字段不存在问题
- 防止代码上线，但是数据库sql未执行导致报错
- **注意** ：该配置会增加项目启动时间，如需关闭请见关闭示例
  
  ```yaml
  # 关闭示例
  batis-plus:
    custom:
      data: false
  ```

## 拦截器

- 基于mp 添加部分觉得不错的拦截器
  - 分页插件拦截器
  - 非法SQL拦截器
  - 防止全表更新删除拦截器
- 详见[mp拦截器配置](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/autoconfigure/MyBatisPlustAutoConfiguration.java)

## 自动填充

- 基于mp 实现部分sql字段自动填充
- 详见[mp自动填充配置](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/autoconfigure/MyBatisPlustAutoConfiguration.java)

## id自增

- 基于mp 实现id自定义自增
- 详见[mpid自增配置](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/autoconfigure/MyBatisPlustAutoConfiguration.java)

## 公共方法封装

- 基于mp 优化部分方法
  详见[Service](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/service) 、 [Model](./third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/model)

</details>


# redis

<details open>
<summary> 点击展开/折叠 redis相关项目描述 </summary>

**注意：使用Spring Cache时请在启动类添加@EnableCaching注解**


## redisson配置

- 使用json方式进行配置（方便对不同环境配置进行区分）

  > 使用示例详见[基于json方式配置redisson客户端](./sample/redis/REDISSONCLIENT.md)


## 多级缓存

- 基于redisson + spring cache + caffeine实现
- [缓存管理器](./third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/autoconfigure/MultiCacheAutoConfiguration.java)
  的主要作用是为每个缓存添加一个缓存管理器，管理由Spring Cache产生的缓存
  > 使用示例详见[基于json方式配置Spring Cache缓存管理器](./sample/redis/CACHE.md) 

- [缓存监听器](./third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/multicache/support/RedissonCaffeineCacheListener.java)
的主要作用是清理分布式下的一级缓存
  > 使用示例详见[缓存监听器](./sample/redis/CACHE.md)，需要在yaml中进行配置其他节点topic

- 多级缓存开关详见[多级缓存配置示例](./sample/redis/CACHE.md)



## 分布式锁

- 基于redisson实现
- 自定义分布式锁[注解](./third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/lock/annotations/Klock.java)
- 自定义注解扩展点：
  1. 开发过程中，不需要关注锁的生命周期，只需关注自己业务即可
  2. 业务结束后，自动释放锁，不需要处理锁的释放时机
  3. 支持多次重试获取锁
  4. 拥有多种获取锁超时、释放锁超时解决方案，且支持自定义解决方案
  5. 支持自定义提示信息
  6. 作用于方法上，可结合[klockKey](./third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/lock/annotations/KlockKey.java)实现锁粒度控制
- [使用示例](./sample/redis/KLOCK.md)
- [具体实现](./third-provider-stater/redis-provider-stater/src/main/java/com/example/redis/provider/stater/lock/handler/KlockAspectHandler.java)

</details>

# 定时任务

<details open>
<summary> 点击展开/折叠 定时任务相关项目描述 </summary>

## 具体实现

- 基于quartz实现，详见[定时任务支持](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support)
- 可视化及页面操作
    - 添加定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中的 **addJob** 方法，添加定时任务，顺便将定时任务信息存库
    - 查询定时任务：从定时任务信息表中获取
    - 修改定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中的 **updateJobCron** 方法，顺便将新信息存库 
    - 删除定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中 **deleteJob** 或 **deleteJobs** 方法，顺便将定时任务信息从库中删除
    - 暂停定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中的 **pauseJob** 方法，顺便更新定时任务信息库
    - 恢复定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中的 **resumeJob** 方法，顺便更新定时任务信息库
    - 立刻执行定时任务：使用页面+接口方式，调用[任务管理器](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzManage.java)
      中的 **runAJobNow** 方法 


- 自动注册定时任务，详见[定时任务工厂](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/factory/QuartzFactory.java)具体实现
  > 该方式虽然配置简单，但是对于自定义参数无法实现，请根据自身情况选择自动注册还是页面操作注册

- 定时任务日志，详见[定时任务执行器配置](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/QuartzExecutionJob.java)具体实现
  > 在执行器中，这里只是做了日志打印，个人感觉，将运行日志存库毕竟好，方便页面查看，请根据自身情况选择

## 配置

### 集群配置

> 详见[集群配置](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/autoconfigure/ScheduleAutoConfiguration.java)具体实现


### 自动注册定时任务

> 配置示例详见[自动注册定时任务](./sample/quartz/AUTO.md)，如果使用可视化配置，请关闭该[配置](./third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/factory/QuartzFactory.java)，减少项目启动时间

### quartz所需表配置

- 建议某个服务单独做定时任务，这样只需要在固定的库添加quartz所需表即可
- [quartz所需表](https://blog.csdn.net/mianyao1004/article/details/105363750)

</details>