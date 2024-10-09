# mp分页使用示例

## 分页插件

> 详情见[mp分页插件注册器](../../third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/autoconfigure/MyBatisPlustAutoConfiguration.java)

## mp分页转换为统一分页返回

- 使用示例：
    ```java
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import com.example.commons.core.model.Pages;
  
    public class TestContorller extends SuperController {
    
        @GetMapping("/v1")
        public List<String> test() {
            //自定义默认分页
            Page<App> page = new Page<>(1, 20, true);
            //分页查询
            Page<App> appPage = appServices.page(page, Wrappers.<App>lambdaQuery().eq(App::getName, "666"));
            //mp分页模型Page转换为Pages
            Pages<App> pages = pages(appPage, App.class);
        }
    }
    ```

- [具体实现：](../../third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/convert/MyBatisPlustPageConverter.java)

## 获取mp默认分页

- 使用示例：
    ```java
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import com.example.commons.core.model.Pages;
  
    public class TestContorller extends SuperController {
    
        @GetMapping("/v1")
        public List<String> test() {
            //默认分页
            Page<App> page = defaultPage(Page.class);
            //分页查询
            Page<App> appPage = appServices.page(page, Wrappers.<App>lambdaQuery().eq(App::getName, "666"));
        }
    }
    ```

- [具体实现：](../../third-provider-stater/mysql-provider-stater/src/main/java/com/example/mysql/provider/stater/mp/convert/MyBatisPlustPageConverter.java)