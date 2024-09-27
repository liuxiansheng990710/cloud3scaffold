# String类型返回值示例

```java
@RestController
public class test {

    @GetMapping("/v1")
    public Responses<String> test() {
        return ResponseUtils.success("21");
    }
}

```

# 自定义返回类型示例

```java
@RestController
public class test {

    @Wrapper
    @GetMapping("/v1")
    public CustomResponse test() {
        return new CustomResponse();
    }
}
```


# 自定义http响应类型返回示例

```java
@RestController
public class test {

    @Wrapper(wrapper = true, httpStatus = HttpStatus.ACCEPTED)
    public List<String> test() {
        return Lists.newArrayList("21", "666");
    }
}
```