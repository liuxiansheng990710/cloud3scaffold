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

- 如需修改请对@RestControllerAdvice()包路径进行修改
- [Wrapper注解](./commons/commons-web/src/main/java/com/example/commons/web/servlet/response/Wrapper.java)辅助使用（仅限于类方法上使用） [示例](./sample/response/RESPONSE.md)
    - 自定义返回类型：如果不需要对返回结果进行[统一包装](./commons/commons-core/src/main/java/com/example/commons/core/model/Responses.java)处理 可添加该注解，在返回时则不进行二次包装
    - 自定义返回http状态码：可对httpStatus字段赋值
    
- String类型返回值 [示例](./sample/response/RESPONSE.md)
  - 请使用[统一返回工具类](./commons/commons-web/src/main/java/com/example/commons/web/utils/ResponseUtils.java)中的success(String object)方法进行返回

### [全局异常处理](./commons/commons-web/src/main/java/com/example/commons/web/servlet/resolver/ServerHandlerExceptionResolver.java)
