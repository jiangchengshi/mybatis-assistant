## Mybatis Assistant（Mybatis助手）

1. 引入数据库驱动包（以MySql为里）

```kotlin
implementation("mysql:mysql-connector-java:latest")
```

2. 配置数据源属性（以MySql为里）

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

> Latest
>
Version: [![Maven Central](https://img.shields.io/badge/Maven-v1.0.4-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:mybatis-assistant-*)

4. 配置mybatis-assistant属性

```properties
mybatis.assistant.mapper-locations=classpath*:mapper/*Mapper.xml
```

或者

```yaml
mybatis:
  assistant:
    mapper-locations: classpath*:mapper/*Mapper.xml
```

### 查询

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

### 逻辑删除

> 测试用例（推荐）：实现IDeletedHandler，完成插入、修改时逻辑删除字段填充

```java
public class MyDeletedHandler implements IDeletedHandler {
}
```

> 逻辑值：

- 0：正常
- -1：删除

### 多租户填充

> 测试用例（推荐）：实现ITenantFillHandler，完成插入时字段值填充

```java
public class MyTenantFillHandler implements ITenantFillHandler {
    @Override
    public Long getTenantId() {
        return 0L;
    }
}
```

### 脱敏

> 测试用例（推荐）：实现IDesensitizeHandler，实体字段添加注解@Desensitize指定策略，完成数据返回时字段脱敏

```java
public class User {
    @Desensitize(strategy = "userName")
    private String name;
}
```

> 默认策略：

- 用户名：userName => 用户名前一后一
- 身份证：idCard => 身份证前三后四
- 移动电话：phoneNumber => 手机号码前三后四

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