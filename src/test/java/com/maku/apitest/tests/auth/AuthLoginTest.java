package com.maku.apitest.tests.auth;

import com.maku.apitest.api.AuthApi;
import com.maku.apitest.tests.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 登录接口测试：/sys/auth/login
 * 一个 Test 类只测一个接口（v3 约定），5 个场景覆盖正向 + 全部异常。
 *
 * 为什么继承 BaseTest：
 * BaseTest 的 @BeforeAll 负责初始化 env/specFactory，子类不需要重复初始化。
 * 此类测的就是 login 本身，BaseTest 里也会调一次 login()，
 * 虽然冗余但不影响结果——只是多发了一次登录请求。
 *
 * 测试实例生命周期（@TestInstance 继承自 BaseTest）：
 * PER_CLASS 保证 @BeforeAll 只执行一次，specFactory 在所有 @Test 间共享，
 * 每个 @Test 调 new AuthApi(specFactory)，得到独立的请求实例，互不干扰。
 */
@Feature("认证管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthLoginTest extends BaseTest {

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("登录")
    @DisplayName("登录-正向：合法账号密码，返回 access_token")
    void should_return_token_when_valid_credentials() {
        new AuthApi(specFactory)
                .login(env.getAuthUsername(), env.getAuthPassword())
                .then()
                // 断言①：HTTP 状态码
                .statusCode(200)
                // 断言②：JSON Schema 结构校验（字段存在性 + 类型）
                // matchesJsonSchemaInClasspath 从 classpath 加载 JSON Schema 文件
                .body(matchesJsonSchemaInClasspath("schemas/auth/login.json"))
                // 断言③：关键业务字段值
                .body("code", org.hamcrest.Matchers.equalTo(0))
                .body("msg", org.hamcrest.Matchers.equalTo("success"))
                // access_token 不为空（说明服务端确实颁发了 token）
                .body("data.access_token", org.hamcrest.Matchers.notNullValue());
    }

    // ──────────────────────── 异常用例（参数化） ────────────────────────

    /*
     * @ParameterizedTest + @CsvFileSource 知识点（第一次出现）：
     * - @ParameterizedTest：将一个测试方法运行多次，每次传入不同参数。
     * - @CsvFileSource：从 CSV 文件读取参数，numLinesToSkip = 1 跳过表头行。
     * - 方法参数对应 CSV 每列，由 JUnit 5 自动注入。
     *
     * // v1 继承：这是从 v1 搬来的最好用的特性，v3 原样保留。
     * // v1 和 v3 的区别：v1 用 Hamcrest equalTo，v3 改用 AssertJ assertThat（更易读）。
     *
     * CSV 路径格式：/ 开头代表 classpath 根目录（src/test/resources/）
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/auth/falselogin.csv", numLinesToSkip = 1)
    @Story("登录")
    @DisplayName("登录-异常：错误凭据应返回 500 + 错误提示")
    void should_return_500_when_wrong_credentials(String description, String username, String password) {
        // 用 AssertJ 的 as() 给断言加上 description，失败时报告会显示更有意义的上下文
        var response = new AuthApi(specFactory).login(username, password);

        assertThat(response.statusCode())
                .as("HTTP 状态码 - 场景：%s", description)
                .isEqualTo(200);

        assertThat((Integer) response.path("code"))
                .as("业务 code - 场景：%s", description)
                .isEqualTo(500);

        assertThat((String) response.path("msg"))
                .as("业务 msg - 场景：%s", description)
                .isEqualTo("用户名或密码错误");

        // 登录失败时 data 应为 null，不应返回任何 token
        assertThat((Object) response.path("data"))
                .as("data 应为 null（无 token）- 场景：%s", description)
                .isNull();
    }
}
