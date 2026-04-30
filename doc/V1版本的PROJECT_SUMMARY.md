# ReqMakuTest 项目总结（含关键代码复盘）

## 1. 项目定位

这是一个以接口自动化测试为核心的测试套件工程，主战场在 `src/test`，核心栈是 `RestAssured + JUnit5 + Allure + JsonPath + JdbcTemplate`。

你这套工程最大的价值，不是“语法新不新”，而是已经具备了完整测试工程思维：分层、数据驱动、断言、报告、清理。

---

## 2. 精华部分（附代码）

### 精华 A：API 层与 testcase 层分离

你把业务接口封装在 `api/*`，用例写在 `testcase/*`，这个架构今天依旧成立。

旧代码片段（好思路，应该保留）：

```java
public class User extends BaseInterface {
    public Response page(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user/page";
        return RestAssured.given()
                .headers("Authorization", token,
                        "Content-Type", "application/x-www-form-urlencoded")
                .queryParams(data)
                .when().get()
                .then().extract().response();
    }
}
```

### 精华 B：参数化 + 异常场景覆盖

你不是只测 happy path，这点很关键。

旧代码片段（测试设计意识很强）：

```java
@ParameterizedTest
@CsvSource({"page,页码不能为空", "limit,每页条数不能为空"})
void test02_false02(String key, String expectMsg) {
    defaultData.delete("$." + key);
    user.page(token, defaultData.json()).then()
            .statusCode(200)
            .body("code", equalTo(500))
            .body("msg", equalTo(expectMsg));
}
```

### 精华 C：不仅校验状态码，还校验结构

这是接口测试质量上限的关键动作。

旧代码片段（非常实用）：

```java
user.page(token, defaultData.json()).then()
    .statusCode(200)
    .body("code", equalTo(0))
    .body(matchesJsonSchemaInClasspath("jsonschema/user/page.json"));
```

### 精华 D：模板化测试数据

`template/params/jsonschema` 的目录组织，说明你当时已经在做数据驱动，不是把 JSON 全塞进 Java 字符串。

---

## 3. 槽点部分（附代码）

### 槽点 A：全局状态耦合重，并发风险高

旧代码片段：

```java
public abstract class BaseInterface {
    protected static String token;
}

public abstract class BaseTest {
    public static String token = "";
    @BeforeAll
    void setAll() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
}
```

问题：静态 token + 全局 RestAssured 配置会导致用例互相影响，未来并行执行时容易串。

### 槽点 B：重复代码太多，维护成本高

`Role/User/Org/Post/...` 里大量重复 `basePath + headers + body + when + then`。

旧代码片段：

```java
response = RestAssured.given().log().all()
        .headers("Authorization", token,
                "Content-Type","application/json")
        .body(data)
        .when().post()
        .then().log().all()
        .extract().response();
```

问题：一旦要改 header、日志策略、超时设置，需要全局手工改很多处。

### 槽点 C：环境与敏感信息硬编码

旧代码片段：

```properties
url=jdbc:mysql://127.0.0.1:3306/maku_boot
username=root
password=mysql
```

问题：迁移环境和团队协作都不方便，且有安全风险。

### 槽点 D：数据清理策略“重锤型”

旧代码片段：

```java
jdbcTemplate.update("delete from sys_org");
jdbcTemplate.update("delete from sys_post");
jdbcTemplate.update("delete from sys_role");
```

问题：在共享库或多人协作中风险较高，容易误伤。

### 槽点 E：依赖老 + 重复声明

典型情况：`json-schema-validator`、`jackson-databind` 重复；多库版本偏老。

---

## 4. 这些“旧知识”请务必保留

1. 按业务模块封装 API 的思想。
2. 测试数据模板化与参数化。
3. 正反场景都测，而不是只测成功路径。
4. 结构化断言（schema）而不只是状态码断言。
5. 前后置清理与可重复执行意识。
6. 测试结果可视化（Allure）意识。

一句话：方法论完全不过时，过时的是实现细节。

---

## 5. 新写法对照（可直接当你下一版模板）

### 5.1 统一请求构建（减少重复）

```java
public abstract class BaseApi {
    protected RequestSpecification req(String token) {
        return RestAssured.given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }
}
```

### 5.2 用配置驱动环境（去硬编码）

```java
public final class Env {
    public static final String BASE_URL =
            System.getProperty("baseUrl", "http://localhost:8080");
}
```

```java
@BeforeAll
static void init() {
    RestAssured.baseURI = Env.BASE_URL;
}
```

### 5.3 用例保持“旧思路 + 新实现”

```java
@ParameterizedTest
@CsvSource({"page", "limit"})
void page_required_field_validation(String key) {
    var payload = loadTemplate("template/user/page.json");
    payload.delete("$." + key);

    userApi.page(token, payload.json())
            .then()
            .statusCode(200)
            .body("code", equalTo(500));
}
```

---

## 6. 最终结论

这套项目不是“该丢掉的老代码”，而是“测试设计成熟、工程实现过时”的项目。

正确打开方式：

1. 保留你的测试设计经验。
2. 用新项目练现代工程写法。
3. 再把高价值模块（Auth/User/Role）回迁到旧工程。

这会比“全盘推翻重来”更快、更稳，也更能形成你的长期方法论。
