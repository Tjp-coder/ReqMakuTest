- # 项目说明
  Maku 系统接口自动化测试框架 v3。

  ## 历史背景
  - v1(三年前):已有完整测试设计思路(分层/参数化/Schema/清理),但实现过时
  - v2:略,本项目从 v1 经验出发重构,目标产业级框架水平

  ## 测试目标系统
  - 系统名:Maku Boot
  - 后端地址:默认 http://localhost:8080
  - 接口风格:RESTful,统一返回 `{code, msg, data}`,业务错误时 HTTP 200 + code 非 0
  - 鉴权方式:登录 `/sys/auth/login` 获取 `data.access_token`,后续接口在 Header 放 `Authorization: <token>`

  # 技术栈(锁定,不要替换)
  - 构建:Maven
  - JDK:17
  - 测试框架:JUnit 5(Jupiter)
  - HTTP:RestAssured 5.4+
  - 字段断言:RestAssured 内置 GPath(`.body("code", equalTo(0))`)
  - 结构断言:json-schema-validator(matchesJsonSchemaInClasspath)
  - 模板数据修改:com.jayway.jsonpath(读 JSON 模板,`.set()` 改字段做异常用例)
  - 通用断言:AssertJ(替代 Hamcrest 大部分场景)
  - 数据驱动:JUnit5 @ParameterizedTest + @CsvFileSource / @MethodSource
  - 报告:Allure 2(allure-junit5 + allure-rest-assured)
  - 失败重试:junit-pioneer @RetryingTest
  - 数据库验证:Spring JdbcTemplate(沿用 v1 思路)
  - 配置:typesafe-config 或 properties + -Denv 切环境

  # 目录约定(严格遵守)
  src/
  ├── main/java/com/maku/apitest/
  │   ├── config/        # Env 配置加载,支持 -Denv 切换
  │   ├── client/        # RequestSpecFactory(全局过滤器、鉴权、超时、日志)
  │   ├── api/           # 业务模块 API 类(AuthApi、UserApi、OrgApi...)
  │   ├── model/         # 请求/响应 POJO(@Data + @Builder + Lombok)
  │   └── utils/         # JsonTemplateUtil(JsonPath 模板修改) / FakerUtil / DbUtil
  └── test/
      ├── java/com/maku/apitest/tests/
      │   ├── base/      # BaseTest(登录、token 缓存、DB 清理)
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

  # 代码风格(强约束)

  ## 必须做
  - 所有接口请求**必须**走 RequestSpecFactory,**禁止**测试类里直接 `RestAssured.given()`
  - 所有业务接口封装到 api/<Module>Api 类,测试类只做"调 API + 断言"
  - 一个 Test 类只测一个接口(例:`UserPageTest` 只测 `/sys/user/page`)
  - 测试方法命名:`should_<期望>_when_<条件>` 或 `<场景>_<正向/异常子类型>`
  - 断言三件套:① 状态码 ② 业务 code/msg ③ 关键字段或 JSON Schema
  - token 通过 BaseTest 注入到子类,**禁止**用 v1 那种 `static String token` 全局变量
  - 配置走 Env 类读 properties,**禁止**硬编码 URL/账号/密码

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

  # 工作流
  - 跑全部:`mvn -Denv=test test`
  - 跑单类:`mvn -Dtest=UserPageTest test`
  - 跑单方法:`mvn -Dtest=UserPageTest#should_return_200_when_valid_params test`
  - 生成报告:`mvn allure:serve`
  - commit:conventional commits(feat/fix/refactor/test/chore)

  # 教学模式(重要)
  本项目作者正在通过这个项目学习现代 Java 接口自动化框架写法。
  - 写代码时,关键类/方法上写**清晰的中文注释**,解释"为什么这么写"(不只是"做什么")
  - 引入新技术点(如 RequestSpecification、JsonSchemaValidator、Lombok 注解等),
    在第一次出现的文件顶部用注释做 3-5 行说明
  - 涉及"v1 → v3 改进点"的地方,注释里标注 `// v1 改进:xxx`,方便对比学习