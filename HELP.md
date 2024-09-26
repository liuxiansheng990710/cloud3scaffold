# 排雷专区

## gradle构建问题

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