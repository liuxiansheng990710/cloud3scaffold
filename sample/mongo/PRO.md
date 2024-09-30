# 开启配置

- 使用自动填充时请添加 **@EnableMongoAuditing** 注解
- 不需要做任何yaml配置

# 全部关闭

所有自定义配置都会关闭

```yaml
  data:
    mongodb:
      custom:
        enable: false
```

# 关闭某个配置（例如自动填充）

仅关闭自动填充配置，其他操作正常进行配置

```yaml
  data:
    mongodb:
      custom:
        auditorAware: false
```
