# mongo分页使用示例


## mongo分页转换为统一分页返回

- 使用示例：
    ```java
    import org.springframework.data.domain.Page;
    import com.example.commons.core.model.Pages;
  
    public class TestContorller extends SuperController {
    
        @GetMapping("/v1")
        public List<String> test() {
            //自定义默认分页
            PageRequest pageRequest = PageRequest.of(0, 20);
            //分页查询
            Page<MongoTest> mongoTests = mongoTestService.get(pageable, "666");
            //mongo分页模型Page转换为Pages
            Pages<MongoTest> pages = pages(mongoTests, MongoTest.class);
        }
    }
    ```

- [具体实现：](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/convert/MongoPageConverter.java)

## 获取mongo默认分页

- 使用示例：
    ```java
    import org.springframework.data.domain.Page;
    import com.example.commons.core.model.Pages;
  
    public class TestContorller extends SuperController {
    
        @GetMapping("/v1")
        public List<String> test() {
            //默认分页
            Pageable pageable = defaultPage(Pageable.class);
            //分页查询
            Page<MongoTest> mongoTests = mongoTestService.get(pageable, "666");
        }
    }
    ```

- [具体实现：](../../third-provider-stater/mongo-provider-stater/src/main/java/com/example/mongo/provider/stater/convert/MongoPageAbleConverter.java)