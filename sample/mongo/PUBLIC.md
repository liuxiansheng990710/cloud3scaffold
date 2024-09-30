# 公共方法使用示例

## 实体类


- mongo实体类基础[BaseMongoModel](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/model/BaseMongoModel.java)
```java
@Document("mongo_test")
public class MongoTest extends BaseMongoModel<Long> {

    @Indexed
    private Long uid;

    private String name;

}
```

## services类
- 直接一起使用
- 继承MongoServiceImpl，实现MongoService

```java
@Service
public class MongoTestServices extends MongoServiceImpl<MongoTest, Long, MongoTestRepository> implements MongoService<MongoTest, Long> {

    public List<MongoTest> get(String name) {
        return baseRepository.findByNameOrderByUidAsc(name);
    }

}
```

## service类
- 实现MongoService
```java
public interface MongoTestService implements MongoService<MongoTest, Long> {

    List<MongoTest> get(String name);

}
```
## serviceImpl类
- 继承MongoServiceImpl
```java
@Service
public class MongoTestService extends MongoServiceImpl<MongoTest, Long, MongoTestRepository> {

    @Override
    public List<MongoTest> get(String name) {
        return baseRepository.findByNameOrderByUidAsc(name);
    }

}
```
