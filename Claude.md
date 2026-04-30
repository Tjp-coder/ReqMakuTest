# 项目说明

Maku 系统接口自动化测试框架 v3。这是一个 Java 接口自动化测试框架, 当前在做 v2 → v3 升级,目标是主流可维护的工程结构。

## 历史背景

- v1(三年前):已有完整测试设计思路(分层/参数化/Schema/清理),但实现过时
- v2:略,本项目从 v1 经验出发重构,目标产业级框架水平

# 测试目标系统

系统名:Maku Boot

后端地址:默认 http://localhost:8080

接口风格:RESTful,统一返回 `{code, msg, data}`,业务错误时 HTTP 200 + code 非 0

鉴权方式:登录 `/sys/auth/login` 获取 `data.access_token`,后续接口在 Header 放 `Authorization: <token>`

## v1 / v2 的处理

- v1 是老代码,作为业务逻辑的"参考资料",特别是鉴权、加签、特殊 Header
- v2 是简化版,作为 v3 的起点,但架构要按下面的目录约定重做
- 任何来自 v1 的业务规则(加密、签名、灰度路由)必须显式确认后迁移,不要"看起来对就抄"

# 技术栈(锁定,不要替换)

- 构建:Maven
- JDK:17
- 测试框架:JUnit 5(Jupiter)

- HTTP/接口测试:RestAssured 5.4+
- 字段断言:RestAssured 内置 GPath(`.body("code", equalTo(0))`)
- 结构断言:json-schema-validator(`matchesJsonSchemaInClasspath`)

- 模板数据修改:com.jayway.jsonpath(读 JSON 模板,`.set()` 改字段做异常用例)

- 通用断言:AssertJ(替代 Hamcrest 大部分场景)

- 数据驱动:JUnit5 @ParameterizedTest + @CsvFileSource / @MethodSource

- 配置:typesafe-config 或 properties + -Denv 切环境

- 日志:SLF4J + Logback

- 报告:Allure 2(allure-junit5 + allure-rest-assured)

- 失败重试:junit-pioneer @RetryingTest

- 数据库验证:Spring JdbcTemplate(沿用 v1 思路)

  # 目录约定(严格遵守)

  ```
  src/
  ├── main/java/com/maku/apitest/
  │   ├── config/        # Env 配置加载,支持 -Denv 切换
  │   ├── client/        # RequestSpecFactory(全局过滤器、鉴权、超时、日志)
  │   ├── api/           # 业务模块 API 类(AuthApi、UserApi、OrgApi...)
  │   ├── model/         # 请求/响应 POJO(@Data + Lombok)
  │   │   ├── common/    # CommonResp<T> / PageResult<T>
  │   │   ├── auth/      # SysTokenVO 等
  │   │   └── user/      # SysUserVO 等
  │   └── utils/         # JsonTemplateUtil / FakerUtil / DbUtil
  └── test/
      ├── java/com/maku/apitest/tests/
      │   ├── base/      # BaseTest(登录、token、DB 清理)
      │   ├── auth/      # 一个接口一个测试类
      │   │   ├── AuthLoginTest.java
    │   │   └── AuthLogoutTest.java
      │   ├── user/
    │   │   ├── UserPageTest.java
      │   │   ├── UserSaveTest.java
      │   │   └── UserUpdateTest.java
      │   └── ...
      └── resources/
          ├── config/    # dev.properties / test.properties / prod.properties
          ├── template/  # JSON 请求模板(沿用 v1 目录结构)
          │   ├── auth/login.json
          │   └── user/page.json
        ├── params/    # CSV/YAML 数据驱动文件
          │   └── auth/falselogin.csv
          ├── schemas/   # JSON Schema(沿用 v1 jsonschema/ 思路)
          │   └── user/page.json
          └── allure.properties
  ```

# 代码风格(强约束)

## 必须做

  - 所有接口请求**必须**走 RequestSpecFactory,**禁止**测试类里直接 `RestAssured.given()`
  - 所有业务接口封装到 `api/<Module>Api` 类,测试类只做"调 API + 断言"
  - 一个 Test 类只测一个接口(例:`UserPageTest` 只测 `/sys/user/page`)
  - 一个测试方法只测一个场景
- 测试方法命名:`should_<期望>_when_<条件>`
- 断言三件套:① 状态码 ② 业务 code/msg ③ 关键字段或 JSON Schema 结构校验
- 禁止裸 `assertEquals`,统一用 AssertJ 的 `assertThat`
- token 通过 BaseTest 注入到子类,**禁止**用 v1 那种 `static String token` 全局变量
- 配置走 Env 类读 properties,**禁止**硬编码 URL/账号/密码
- 每个新接口至少一个正向用例 + 一个异常用例(参数缺失/越权/边界)

## 禁止做

  - ❌ `RestAssured.basePath = "..."` 这种修改全局静态变量(v1 老坑,并发会爆)
  - ❌ `static String token` 全局共享(同上)
  - ❌ 在测试代码里拼 JSON 字符串
  - ❌ `delete from xxx` 不带条件的全表清理(改用按 username/code 精确清理)
  - ❌ 一个 Test 类塞多个接口的用例

## v1 经验保留(明确继承)

  - ✅ JsonPath 模板 + `.set()` / `.delete()` 改字段做异常用例(超好用,继续用)
  - ✅ 按模块组织 template/params/schemas 三个目录
  - ✅ JdbcTemplate 做数据库结果验证和清理
  - ✅ 异常场景覆盖意识(必填项缺失、未授权、边界值)
  - ✅ Schema 结构断言(matchesJsonSchemaInClasspath)
  - ✅ Allure 注解 @Feature/@Story/@DisplayName

# POJO 与请求模板的边界(必读)

## 总体策略

  - **请求侧**:全部用 JsonTemplateUtil + JSON 模板,**不建请求 POJO** 理由:异常用例需要灵活删字段/改字段,模板比 POJO 灵活
  - **响应侧**:按需建 POJO,壳子用 CommonResp<T> 统一,业务部分按 VO 建

## 何时建响应 POJO

  - ✅ **必须建**:响应字段会被多个用例断言,或要从响应里取数据给后续用例(如 token、id)
  - ✅ **必须建**:列表/分页接口(用 PageResult<T> 泛型壳)
  - ❌ **不要建**:只断言 code/msg 的接口(删除、改密码、登出、导出文件等)
  - ❌ **不要建**:一次性接口、字段超多但只断言 1-2 个的

## POJO 结构规则

  - 通用响应壳:`com.maku.apitest.model.common.CommonResp<T>` 字段:code(Integer)/msg(String)/data(T)
  - 通用分页壳:`com.maku.apitest.model.common.PageResult<T>` 字段:total(Integer)/list(List<T>)
  - 业务 VO:命名直接复用 Maku 后端的 VO 名(如 SysUserVO、SysTokenVO), 放在 `model/<模块>/` 目录下

## POJO 编写规范

  - 必须用 Lombok `@Data`
  - 必须加 `@JsonIgnoreProperties(ignoreUnknown = true)`(防止接口加字段后 POJO 挂)
  - 字段名和后端 JSON 不一致时用 `@JsonProperty` 映射(如 access_token → accessToken)
  - 类顶部用中文注释说明:对应 Maku 哪个接口、哪个 VO

## 测试代码结构(强制四段式)

  每个测试方法严格按这四段写:

```java
  // ① 准备:用模板构造请求体(GET 请求跳过)
  Map<String, ?> body = JsonTemplateUtil.load("template/user/save.json")
          .set("$.username", "test_001")
          .toMap();
  
  // ② 调用:Api 类发请求,返回 Response
  Response response = userApi.save(token, body);
  
  // ③ 解析:用 POJO 解析响应(只在需要取字段或多字段断言时)
  CommonResp<SysUserVO> resp = response.as(new TypeRef<CommonResp<SysUserVO>>() {});
  
  // ④ 断言:HTTP 层 + 业务层 + 字段层
  response.then().statusCode(200);
  assertThat(resp.getCode()).isEqualTo(0);
  assertThat(resp.getData().getUsername()).isEqualTo("test_001");
```

  异常用例可省略 ③(不需要 POJO 解析,直接 body 断言):

```java
  response.then().statusCode(200)
          .body("code", equalTo(500))
          .body("msg", equalTo("用户名不能为空"));
```

## 反例(看到要拒绝)

  - ❌ 建 UserSaveReq、UserPageQuery 这种请求 POJO
  - ❌ 把 CommonResp 和具体 VO 揉在一个文件里
  - ❌ POJO 不加 @JsonIgnoreProperties,接口加字段全挂
  - ❌ 异常用例为了用 POJO 解析强行建 ErrorResp

## JsonTemplateUtil 输出方式选择(强约束)

| HTTP 方法 | RestAssured 调用 | 用 |
|---|---|---|
| POST/PUT 带 body | `.body(tpl.toJson())` | `toJson()` 返回 JSON 字符串 |
| GET 带 query | `.queryParams(tpl.toMap())` | `toMap()` 返回 Map |

JsonTemplateUtil 的方法注释必须明示这个对应关系:
- `toJson()` 注释:"转为 JSON 字符串,用于 POST/PUT 请求体 `.body()`"
- `toMap()` 注释:"转为 Map,用于 GET 请求查询参数 `.queryParams()`"

反例(看到要拒绝):
- ❌ POST 用 `toMap()` + `.body(map)`(可能被当作 form 参数序列化)
- ❌ GET 用 `toJson()` + `.body(json)`

# 工作流

  - 跑全部:`mvn -Denv=test test`
  - 跑单类:`mvn -Dtest=UserPageTest test`
  - 跑单方法:`mvn -Dtest=UserPageTest#should_return_200_when_valid_params test`
  - 提交前跑:`mvn verify`(包含 checkstyle 如果配了)
  - 生成报告:`mvn allure:serve`
  - commit message:conventional commits(feat/fix/refactor/test/chore)

# 教学模式(重要)

  本项目作者正在通过这个项目学习现代 Java 接口自动化框架写法。

  - 写代码时,关键类/方法上写**清晰的中文注释**,解释"为什么这么写"(不只是"做什么")
  - 引入新技术点(如 RequestSpecification、JsonSchemaValidator、Lombok 注解等), 在第一次出现的文件顶部用注释做 3-5 行说明
  - 涉及"v1 → v3 改进点"的地方,注释里标注 `// v1 改进:xxx`,方便对比学习