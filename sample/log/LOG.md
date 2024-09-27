# 请求日志配置示例

```yaml
server:
  port: 2101

spring:
  application:
    name: provider-base
  profiles:
    active: alpha

logging:
  config: file:./commons/commons-core/src/main/resources/log4j2-local.xml
```
- **全局日志配置文件必须配置 详见：logging.config**
- 最好配置服务名和环境，这样打印出来的日志更有效果
  - 服务名的两种配置方式 [详见](../../commons/commons-web/src/main/java/com/example/commons/web/servlet/enums/Servers.java)
    - vm options添加：-Dserver.name=base
    - yaml中配置：spring.application.name:provider-base
    
  - 环境的两种配置方式 [详见](../../commons/commons-web/src/main/java/com/example/commons/web/servlet/enums/ServerEnvironment.java)
    - vm options添加：-Dserver.environment=alpha
    - yaml中配置：spring.profiles.active:alpha
