# ReqMakuTest

基于 [MakuBoot](https://github.com/Tjp-coder/ReqMakuTest) 后端系统的接口自动化测试框架，个人学习项目，记录从 v1 到 v3 的迭代重构过程。

---

## 项目背景

v1 已实现完整的测试思路（分层设计 / JSON 模板 / 参数化 / Schema 验证 / DB 清理），但存在并发安全隐患（`static token`、全局 `RestAssured` 静态变量）和工程化不足（硬编码 URL、无环境切换）。v3 在保留 v1 优秀实践的基础上，对框架层做了系统性重构。

---

## 最终框架效果

### 核心能力

| 能力 | 实现方式 |
|------|----------|
| 多环境切换 | `-Denv=test/dev/prod`，读 `config/<env>.properties` |
| 线程安全 HTTP | `RequestSpecFactory` 每次返回新 `RequestSpecification` 实例，无全局静态变量 |
| JSON 模板数据驱动 | `JsonTemplateUtil` 流式 API，`.set()` / `.delete()` 构造正/异常用例 |
| 参数化测试 | JUnit5 `@ParameterizedTest` + `@CsvFileSource` / `@MethodSource` |
| 三层断言 | 状态码 → 业务 code/msg（RestAssured GPath）→ JSON Schema 结构断言 |
| 数据库验证 | Spring `JdbcTemplate` + Druid 连接池，精确断言写库结果 |
| 失败重试 | junit-pioneer `@RetryingTest` |
| 测试报告 | Allure 2，HTTP 请求/响应自动附加到报告（`allure-rest-assured` Filter） |

### 目录结构

```
src/
├── main/java/com/maku/apitest/
│   ├── config/          # Env — 多环境配置加载
│   ├── client/          # RequestSpecFactory — HTTP 规格工厂（线程安全）
│   ├── api/             # 业务 API 类（AuthApi、UserApi...）
│   ├── model/           # 请求/响应 POJO（Lombok @Data + @Builder）
│   └── utils/           # JsonTemplateUtil、FakerUtil、DbUtil
└── test/
    ├── java/com/maku/apitest/tests/
    │   ├── base/        # BaseTest — 框架初始化、登录、token 注入
    │   ├── auth/        # AuthLoginTest、AuthLogoutTest
    │   ├── user/        # UserPageTest、UserSaveTest、UserUpdateTest...
    │   └── ...          # 其他模块（org / role / post / attachment）
    └── resources/
        ├── config/      # test.properties / dev.properties
        ├── template/    # JSON 请求模板
        ├── params/      # CSV 数据驱动文件
        └── schemas/     # JSON Schema 文件
```

### 架构关键决策

- **`BaseTest` 用 `@TestInstance(PER_CLASS)`**：整个测试类共享一个实例，`@BeforeAll` 只登录一次，token 作为实例字段注入子类，彻底消除 v1 的 `static token` 并发问题
- **`RequestSpecFactory` 不可变 `baseSpec`**：`RequestSpecBuilder.build()` 产出 `FilterableRequestSpecification`，每次 `given().spec(baseSpec)` 都是新对象，并发安全
- **框架代码放 `src/main/java`**：与测试代码分离，层次清晰，可被多个测试模块复用

---

## 技术栈

| 类别 | 技术 |
|------|------|
| 构建 | Maven |
| JDK | 17 |
| 测试框架 | JUnit 5 (Jupiter) |
| HTTP 客户端 | RestAssured 5.4.0 |
| 数据修改 | JsonPath (jayway) |
| 断言 | AssertJ + RestAssured GPath + json-schema-validator |
| 数据库 | Spring JdbcTemplate 6 + Druid + MySQL 8 |
| 报告 | Allure 2 |
| 失败重试 | junit-pioneer |

---

## 快速开始

```bash
# 运行测试（需本地启动 MakuBoot 服务）
mvn -Denv=test test

# 查看 Allure 报告
mvn allure:serve
```

---

## 版本历史

| 版本 | 说明 |
|------|------|
| v1 | 最初实现，测试思路完整，工程化不足，已归档至 `src/test/java/v1/` |
| v2 | 中间探索版本，已归档至 `src/test/java/v2/` |
| v3 | 当前版本，框架层系统重构，代码在 `src/main/java/com/maku/apitest/` |
