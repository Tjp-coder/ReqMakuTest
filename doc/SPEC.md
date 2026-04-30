# Maku 接口自动化框架 v3 改造规范

## 1. 目标
- 把 v1 的成熟测试设计思路,用现代工程实现重新落地
- 保留 v1 全部测试经验(模板/参数化/Schema/数据库验证/Allure)
- 升级到主流工程标准(请求工厂、配置驱动、并发安全、教学注释)
- 单接口用例编写时间从 v1 的 ~15 分钟降到 < 5 分钟

## 2. 核心组件设计

### 2.1 Env(配置加载)
- 读取 `src/test/resources/config/<env>.properties`
- 支持 `-Denv=dev/test/prod` 切换
- 必须暴露:baseUrl / port / dbUrl / dbUsername / dbPassword / authUsername / authPassword

### 2.2 RequestSpecFactory
- 提供两种 RequestSpecification:
  - `unauth()`:无鉴权(用于登录、注册)
  - `auth(String token)`:带 Authorization Header
- 默认挂载:
  - RequestLoggingFilter / ResponseLoggingFilter(失败时打印)
  - AllureRestAssured filter(报告自动附加请求/响应)
  - 超时配置(connection 5s / socket 30s)
- **替代 v1 的 `RestAssured.basePath = "..."` 全局污染写法**

### 2.3 BaseApi
- 子类(AuthApi/UserApi)通过 `req(token)` 拿到配好的 RequestSpec
- 不再继承 BaseInterface 那种带 protected response 字段的设计

### 2.4 BaseTest
- @BeforeAll:加载 Env、初始化 RestAssured 默认配置(只设 baseUri 和 port)
- @BeforeAll:登录拿 token,**作为实例字段**保存(不是 static)
- @AfterAll:精确清理本测试类创建的数据(按 username 前缀或测试标记字段)
- 提供 jdbcTemplate 给子类做 DB 验证

### 2.5 JsonTemplateUtil(沿用 v1 思路,API 化封装)
```java
JsonTemplateUtil.load("template/user/page.json")    // 加载模板
    .set("$.username", "test01")                     // 改字段
    .delete("$.password")                            // 删字段(测必填)
    .toMap();                                        // 转 Map 给 RestAssured
```

### 2.6 测试类组织(关键改进)
**v1 老写法**(乱):
```
testcase/user/UserTest.java  ← 一个文件测 page/save/update/delete 多个接口
```

**v3 新写法**(清晰):
```
tests/user/
├── UserPageTest.java        ← 只测 /sys/user/page
│   ├── should_return_success_when_valid_params()
│   ├── should_return_500_when_missing_required_field(page/limit)
│   ├── should_return_success_when_missing_optional_field(order/asc/name)
│   └── should_return_401_when_no_token()
├── UserSaveTest.java
└── UserUpdateTest.java
```

## 3. 改造阶段拆分

### 阶段 0:学习准备(0.5 天)
- 阅读已生成的骨架,理解每个文件的作用
- 跑通 Hello World 测试,熟悉 Maven + Allure 流程
- **验收:能独立解释 RequestSpecFactory、BaseApi、BaseTest 各自职责**

### 阶段 1:工程骨架(0.5 天)
- pom.xml(完整依赖 + Allure 插件 + Surefire JUnit5 配置)
- Env / RequestSpecFactory / BaseApi / BaseTest
- JsonTemplateUtil
- 配置文件:test.properties
- **验收:`mvn test` 跑通(此时 0 个真实用例)**

### 阶段 2:Auth 模块迁移(1 天,作为学习样板)
- model:LoginReq / LoginResp(POJO + Lombok)
- api:AuthApi(login + logout)
- template:auth/login.json
- params:auth/falselogin.csv
- schemas:auth/login.json
- tests:AuthLoginTest(正向/账号错/密码错/账号空/密码空)
       AuthLogoutTest(正向/未授权)
- **验收:`mvn -Dtest=AuthLoginTest test` 全绿,Allure 报告完整**

### 阶段 3:User 模块迁移(1.5 天,自己练手)
- 完整迁移 v1 的 UserTest,但拆成 UserPageTest / UserSaveTest / UserUpdateTest / UserDeleteTest
- 每个 Test 类至少 4 个场景(正向/必填缺失/未授权/异常值)
- 引入 JdbcTemplate 验证 save/update 后的 DB 数据
- **验收:用例数 >= 16,全绿,DB 清理无残留**

### 阶段 4:其他模块批量迁移(2-3 天)
- Org / Post / Role / Attachment 按 User 模式复制
- 边迁移边沉淀公共模式(比如分页接口的通用断言)

### 阶段 5:CI + 报告打磨(0.5 天)

- GitHub Actions 或 Jenkinsfile
- Allure 报告归档 + GitHub Pages 展示(简历加分项)
- README 写明跑测试/看报告的步骤

## 4. 学习路线图(按你的情况定制)

读 v3 代码时,按这个顺序看,每搞懂一个就能写一类用例:

| 顺序 | 文件                    | 学到的知识点                                |
| ---- | ----------------------- | ------------------------------------------- |
| 1    | pom.xml                 | Maven 依赖管理、Surefire 配置、Allure 插件  |
| 2    | Env.java                | -D 系统属性、Properties 加载、单例          |
| 3    | RequestSpecFactory.java | RestAssured 链式 API、Filter 机制、超时配置 |
| 4    | BaseApi.java            | 抽象类、模板方法、依赖注入思想              |
| 5    | BaseTest.java           | JUnit5 生命周期注解、@TestInstance          |
| 6    | AuthApi.java            | 业务接口封装、POJO 序列化                   |
| 7    | LoginReq.java           | Lombok @Data/@Builder、Jackson 注解         |
| 8    | AuthLoginTest.java      | @Test + @ParameterizedTest + Allure 注解    |
| 9    | login.json schema       | JSON Schema 语法、required/properties/type  |
| 10   | JsonTemplateUtil.java   | JsonPath 操作、流式 API 设计                |

## 5. 风险点
- v1 的 JsonPath 模板用法和 RestAssured GPath 容易混淆,需注释说明
- 老 token 静态变量改造后,部分依赖顺序的用例可能失败,需重构
- DB 清理从"全表 delete"改"精确 delete",要先确认每条测试数据的标识字段