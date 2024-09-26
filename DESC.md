# 项目相关描述

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

- [x] 如需修改请对@RestControllerAdvice()包路径进行修改

### [全局异常处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/ServerHandlerExceptionResolver.java)
