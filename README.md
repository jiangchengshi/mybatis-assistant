## Mybatis Assistant（Mybatis助手）

1. 引入数据库驱动包

```kotlin
implementation("mysql:mysql-connector-java:latest")
```

2. 配置数据源属性

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.13.213:3336/mybatis-assistant
spring.datasource.username=root
spring.datasource.password=1234.abcd
```

或者

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.13.213:3336/mybatis-assistant
    username: root
    password: 1234.abcd
```

3. 引入mybatis-assistant-boot-starter包

```kotlin
implementation("cool.doudou:mybatis-assistant-boot-starter:latest")
```

4. 配置mybatis-assistant属性

```properties
mybatis.assistant.mapper-locations=classpath*:mapper/*Mapper.xml
mybatis.assistant.interceptors=fieldFill,tenantFill,desensitize,log
```

或者

```yaml
mybatis:
  assistant:
    mapper-locations: classpath*:mapper/*Mapper.xml
    interceptors:
      - fieldFill
      - tenantFill
      - desensitize
      - log
```

### 查询（默认启用）

> 测试用例（推荐）：分页参数PageDTO实体中赋值pageNum、pageSize即可

```java
public class pageServiceTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void select(PageDTO<User> pageDTO) {
        LambdaQuery<User> lambdaQuery = new LambdaQuery<>();
        // pageDTO.data 赋值 LambdaQuery
        List<User> userList = userMapper.selectPage(pageDTO.page(), lambdaQuery);
    }
}
```

### 字段填充

> 测试用例（推荐）：实现IFieldFillHandler，完成插入、修改时字段值填充

```java
public class MyFieldFillHandler implements IFieldFillHandler {
    @Override
    public void insert(MetaObject metaObject) {
        this.fill(metaObject, "createBy", 1L);
        this.fill(metaObject, "createTime", LocalDateTime.now());
    }

    @Override
    public void update(MetaObject metaObject) {
        this.fill(metaObject, "updateBy", 1L);
        this.fill(metaObject, "updateTime", LocalDateTime.now());
    }
}
```

### 脱敏

> 测试用例（推荐）：实体字段添加注解@Desensitize，指定策略

```java
public class User {
    @Desensitize(strategy = StrategyEnum.USER_NAME)
    private String name;
}
```

### 多租户

> 开发中...

### 逻辑删除

> 开发中...

### 代码生成器

> 测试用例（推荐）

```java
public class CodeGenTests {

    @Test
    public void execute() {
        CodeGenerator.create("192.168.13.213", 3336, "root", "1234.abcd")
                .globalConfig(new GlobalConfig().author("test"))
                .packageConfig(new PackageConfig().module("user"))
                .tableConfig(new TableConfig().schema("mybatis-assistant").nameList("sys_user"))
                .execute();
    }
}
```