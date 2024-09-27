# 项目相关描述

## 项目说明

```
cloud3scaffold
└── commons -- 系统公共模块
     ├── commons-core -- 公共配置/依赖包
            ├── exceptions -- 公共异常
            ├── log -- 日志配置
                 ├── mysql -- mysql日志
                 ├── mongo -- mongo日志
                 ├── mq -- mq日志
            ├── model -- 公共实体
            ├── utils -- 工具类
            ├── resources/log4j2-local.xml -- 全局日志输出配置
     ├── commons-web -- web配置包
            ├── autoconfigure -- web自动装配
            ├── servlet -- web servlet配置
                  ├── resolver -- web异常全局处理（包括异常日志）
                  ├── response -- web响应全局处理（包括请求日志）
                  ├── undertow -- undertow配置 
                  ├── interceptors -- 拦截器配置 
                  ├── model -- 公共实体
            ├── runner -- 启动日志配置类
            ├── utils -- 工具类 


```

## gradle

### 统一依赖管理

基于 [gradle catalog](https://docs.gradle.org/current/userguide/platforms.html) 中的TOML方式实现,
本项目使用groovy方式实现，如需替换为kotlin可参考[gradle kotlin 依赖管理](https://blog.csdn.net/qq_36279799/article/details/131983818)

- [x] gradle 7 之后推荐该用法
- [x] 简化 build.gradle 文件
- [x] 父模块定义dependencies 规范 子模块共享依赖版本

## web配置

### [undertow](./commons/commons-web/src/main/java/com/example/commons/web/servlet/undertow/UndertowServerFactoryCustomizer.java)

### [全局响应处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/CommonsControllerAdvice.java)

- 如需修改请对@RestControllerAdvice()包路径进行修改
- [Wrapper注解](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/Wrapper.java)
  辅助使用（仅限于类方法上使用） [示例](./sample/response/RESPONSE.md)
    - 自定义返回类型：如果不需要对返回结果进行[统一包装](./commons/commons-core/src/main/java/com/example/commons/core/model/Responses.java)处理
      可添加该注解，在返回时则不进行二次包装
    - 自定义返回http状态码：可对httpStatus字段赋值

- String类型返回值 [示例](./sample/response/RESPONSE.md)
    - 请使用[统一返回工具类](./commons/commons-web/src/main/java/com/example/commons/web/utils/ResponseUtils.java)中的success(String
      object)方法进行返回

### [全局异常处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/ServerHandlerExceptionResolver.java)

### web日志全局配置

- [正常请求日志](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/CommonsControllerAdvice.java)
- [错误请求日志](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/GlobalExceptionHandler.java)
