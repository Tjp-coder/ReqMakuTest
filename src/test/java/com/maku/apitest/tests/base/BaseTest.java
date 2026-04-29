package com.maku.apitest.tests.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.maku.apitest.client.RequestSpecFactory;
import com.maku.apitest.config.Env;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.JdbcTemplate;
/*
 * 职责：所有测试类的抽象基类，负责框架初始化、登录获取 token、提供数据库验证工具。
 *
 * 核心概念：
 * - @TestInstance(PER_CLASS)：JUnit5 默认为每个 @Test 方法创建一个新的测试类实例，
 *   这意味着 @BeforeAll 方法必须是 static 的，实例字段也会被重置。
 *   PER_CLASS 改为整个测试类只创建一个实例，@BeforeAll 可以是非静态方法，
 *   实例字段（如 token）在所有测试方法间共享，只初始化一次（只登录一次）。
 * - 为什么是 abstract class：防止被直接实例化，同时可以持有 protected 字段供子类使用。
 * - 为什么 login() 设计为可覆盖的 protected 方法：
 *   部分测试类可能需要用不同账号登录（如测试权限差异），子类可以 @Override 返回不同 token。
 *
 * // v1 改进：v1 的 token 是 `public static String token`，所有测试类共享同一个静态字段。
 * //          若并发运行多个测试类，类 A 登录拿到的新 token 会覆盖类 B 正在用的 token，
 * //          导致类 B 的请求全部返回 401。
 * //          v3 改为 protected 实例字段，每个测试类独立持有自己的 token，并发安全。
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    // 实例字段（非 static），每个测试类独立持有，并发安全
    protected Env env;
    protected RequestSpecFactory specFactory;

    // v1 改进：从 static 改为实例字段，见类顶部注释
    protected String token;

    // JdbcTemplate：封装 JDBC 样板代码，子类用它做 DB 结果验证和测试数据清理
    protected JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUpFramework() {
        env = Env.get();

        /*
         * enableLoggingOfRequestAndResponseIfValidationFails：
         * 只在断言失败时才把请求/响应打印到控制台，正常通过时静默。
         * 这是一个全局设置，但只影响"是否打日志"，不影响请求本身的并发安全性。
         * Allure Filter 已经在 RequestSpecFactory 里配置好，失败时可以在报告里看。
         */
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        specFactory = new RequestSpecFactory(env);
        jdbcTemplate = buildJdbcTemplate(env);

        // 登录拿 token，子类可 @Override login() 实现不同账号登录
        token = login();
    }

    /**
     * 登录并返回 access_token。
     * Phase 1（骨架阶段）：返回空字符串，仅作占位符。
     * Phase 2（Auth 模块完成后）：替换为 new AuthApi(specFactory).loginAndGetToken(env)。
     *
     * 设计为 protected 而非 private，允许子类覆盖以使用不同账号（如测试权限隔离场景）。
     */
    protected String login() {
        // TODO Phase 2 完成后替换为真实登录实现：
        // return new com.maku.apitest.api.AuthApi(specFactory).loginAndGetToken(env);
        return "";
    }

    /*
     * 为什么用 DruidDataSource 而不是 v1 的 JDBCUtils（从 druid.properties 读）：
     * v3 的 DB 连接信息统一从 Env 来，Env 从 config/test.properties 读，
     * 这样数据库配置和其他配置（baseUrl/账号）在同一个地方管理，不用维护两份配置文件。
     */
    private static JdbcTemplate buildJdbcTemplate(Env env) {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(env.getDbUrl());
        ds.setUsername(env.getDbUsername());
        ds.setPassword(env.getDbPassword());
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return new JdbcTemplate(ds);
    }
}
