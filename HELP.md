# 排雷专区

## 目录

- [gradle构建问题](#gradle构建问题)

- [mysql相关问题](#mysql相关问题)

- [启动日志相关问题](#启动日志问题)


## gradle构建问题

<details>
<summary> 点击展开/折叠 gradle构建问题 </summary>

### - Problem: In version catalog libs, you can only call the 'from' method a single time
> 这里在首次构建失败后再次构建时，可能在setting.gradle会出现该报错
- 此时需要将versionCatalogs下的libs改个名 重新构建 成功后再改回libs即可
- 或者直接删除.gradle文件 重新构建

### - Could not find method api() for arguments... 
这是因为api引入依赖的方式是java-library插件中的方法，而非java插件的 [官方文档](https://docs.gradle.org/current/userguide/java_library_plugin.html)
- 解决方式：id 'java-library'

### - 构建乱码

- File -> Settings -> Editor -> File Encodings
  - Global Encoding、Project Encoding、Properties Files都选择**UTF-8**
  
- Help -> Edit Custom VM Option
  - 追加 -Dfile.encoding=UTF-8 到文档末尾 重启idea生效
  
  
### - compileJava命令报错

- Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.3.4
- 无效的源发行版: 17
  - 这是两个问题都是因为Java版本与gradle所需构建版本不一致导致
  - File -> setting -> build tools -> gradle 设置sdk版本
  - 查看本地环境变量是否配置了java环境

</details>

## mysql相关问题

<details open>
<summary> 点击展开/折叠 mysql相关问题 </summary>

### - Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.

> 结合p6spy使用的时候，如果是mysql8.x 则需要使用com.mysql.cj.jdbc.Driver，所以在spy.properties文件中，修改**driverlist**



</details>

## 启动日志问题

<details open>
<summary> 点击展开/折叠 启动日志问题 </summary>

### - main WARN The use of package scanning to locate plugins is deprecated and will be removed in a future release

**main WARN No Root logger was configured, creating default ERROR-level Root logger with Console appender**

> 这是因为nacos与log4j2版本兼容问题导致，详见[议题](https://github.com/alibaba/nacos/issues/12102)

> 未来可能会升级版本解决该问题，目前先添加系统属性规避

### - Bean 'org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration$DeferringLoadBalancerInterceptorConfig' of type [org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration$DeferringLoadBalancerInterceptorConfig] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). The currently created BeanPostProcessor [lbRestClientPostProcessor] is declared through a non-static factory method on that class; consider declaring it as static instead.
- Bean 'deferringLoadBalancerInterceptor' of type [org.springframework.cloud.client.loadbalancer.DeferringLoadBalancerInterceptor] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [lbRestClientPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
- Bean 'org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration' of type [org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). The currently created BeanPostProcessor [loadBalancerWebClientBuilderBeanPostProcessor] is declared through a non-static factory method on that class; consider declaring it as static instead.
- Bean 'org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration$ReactorDeferringLoadBalancerFilterConfig' of type [org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration$ReactorDeferringLoadBalancerFilterConfig] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [loadBalancerWebClientBuilderBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
- Bean 'reactorDeferringLoadBalancerExchangeFilterFunction' of type [org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [loadBalancerWebClientBuilderBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.

> 问题详见 [cloud commons issues](https://github.com/spring-cloud/spring-cloud-commons/issues/1315) 在 [4.1.5](https://github.com/spring-cloud/spring-cloud-commons/pull/1361) （截止到24.10.22暂未发布） 中会解决这个问题

> 现在spring-cloud-commons的版本是4.1.4，之后会考虑是升级cloud版本，还是强引用4.1.5的

</details>