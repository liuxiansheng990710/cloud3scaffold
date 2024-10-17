# 自动注册定时任务所需配置

## 添加定时任务基础信息

> [QuartzJobEnum](../../third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/enums/QuartzJobEnum.java)中进行添加
主要添加定时任务bean、cron、id、任务名等

> 配置示例

```java
public enum QuartzJobEnum {
    JOB_TEST(123L, "测试job", "com.example.quartz.provider.stater.support.Test", null, "0/5 * * * * ? *", false, "备注");
}
```

## 定时任务实现配置

> 实现[IExecuteQuartzJob](../../third-provider-stater/quartz-provider-stater/src/main/java/com/example/quartz/provider/stater/support/IExecuteQuartzJob.java)，在execute方法中书写定时任务，添加@Componet

> 配置示例

```java
@Slf4j
@Component
public class Test implements IExecuteQuartzJob {

    @Override
    public void execute(Long jobId, JSONObject parm) {
        log.info("执行" + new Date());
    }

}

```