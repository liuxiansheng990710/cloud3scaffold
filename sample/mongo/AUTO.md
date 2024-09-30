# id自动填充示例

- mongo实体类基础[BaseMongoModel](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/model/BaseMongoModel.java)

- 该实体在插入时会自动对id进行Long类型值的填充

```java

@Document("mongo_test")
public class MongoTest extends BaseMongoModel<Long> {

    @Indexed
    private Long uid;

    private String name;

}
```

# 创建时间、修改时间自动填充示例

- mongo实体类基础[BaseMongoModel](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/model/BaseMongoModel.java)

- 注意不要进行手动填充id的值，否则创建时间会失效
- 该实体在插入时会自动对创建时间，修改时间进行ISODate类型值的填充

```java

@Document("mongo_test")
public class MongoTest extends BaseMongoModel<Long> {

    @Indexed
    private Long uid;

    private String name;

}
```

# 创建人、修改人信息自动填充示例

- mongo实体类基础[BaseMongoModel](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/model/BaseMongoModel.java)

- 注意不要进行手动填充id的值，否则创建人会失效
- 该实体在插入时会自动对创建人，修改人信息进行填充

```java
@Document("mongo_test")
public class MongoTest extends BaseMongoModel<Long> {

    @Indexed
    private Long uid;

    private String name;

}