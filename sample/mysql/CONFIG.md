# mysql相关自定义配置示例

## 日志配置示例

- yaml配置

```yaml
spring:
  datasource:
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql://127.0.0.1:3306
```

- [spy.properties](../log/spy.properties) 请根据mysql版本，设置对应的 **driverlist**
