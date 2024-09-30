# 排雷专区

## 目录

- [gradle构建问题](#gradle构建问题)

- [mysql相关问题](#mysql相关问题)


## gradle构建问题

<details>
<summary> 点击展开/折叠 gradle构建问题 </summary>

### - Problem: In version catalog libs, you can only call the 'from' method a single time
这里在首次构建失败后再次构件时，可能在setting.gradle会出现该报错
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
<summary> 点击展开/折叠 项目说明 </summary>

### - Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.

结合p6spy使用的时候，如果是mysql8.x 则需要使用com.mysql.cj.jdbc.Driver，所以在spy.properties文件中，修改**driverlist**



</details>