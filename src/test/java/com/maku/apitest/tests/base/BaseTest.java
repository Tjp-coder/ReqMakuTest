package com.maku.apitest.tests.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.maku.apitest.api.AuthApi;
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
     * 登录并返回 access_token，在 @BeforeAll 中执行一次，结果缓存到 this.token。
     *
     * 设计为 protected 而非 private，允许子类覆盖以使用不同账号（如测试权限隔离场景）。
     * 例：需要测试普通用户权限的测试类可以 @Override login() 返回普通用户的 token。
     */
    protected String login() {
        return new AuthApi(specFactory).loginAndGetToken(env);
    }

    /**
     * 确保 sys_org 表中存在测试机构 test_v3_org，返回其 id（幂等，多次调用安全）。
     *
     * 使用 WHERE NOT EXISTS 保证单条记录，避免 INSERT IGNORE 在无唯一索引时重复插入。
     * 测试机构是跨模块的测试基础设施：凡需要创建用户的测试类（User/Org/Post 等）
     * 都可在 @BeforeAll 中调用此方法，不需要各自重复实现。
     *
     * 注：测试机构不在测试结束后删除，因为它是共享基础设施，其他测试类可能仍在使用。
     * 测试产生的「用户数据」由各自测试类的 @AfterEach 负责清理，与机构数据分开管理。
     */
    protected Long ensureTestOrg() {
        jdbcTemplate.update(
                "INSERT INTO sys_org (name, pid, sort, version, deleted, creator) " +
                "SELECT 'test_v3_org',0,999,0,0,10000 WHERE NOT EXISTS " +
                "(SELECT 1 FROM sys_org WHERE name='test_v3_org' AND deleted=0)");
        return jdbcTemplate.queryForObject(
                "SELECT id FROM sys_org WHERE name='test_v3_org' AND deleted=0 LIMIT 1", Long.class);
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
        // 连接超时 5 秒：DB 不通时快速失败，而不是死等到 CI timeout-minutes 强杀
        ds.setConnectTimeout(5000);   // TCP 握手超时
        ds.setSocketTimeout(10000);   // 单次 SQL 读写超时
        ds.setMaxWait(10000);         // 从连接池拿连接最多等 10 秒，超时立即抛异常
        return new JdbcTemplate(ds);
    }
}
