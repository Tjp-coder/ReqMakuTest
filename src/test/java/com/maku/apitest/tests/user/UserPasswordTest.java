package com.maku.apitest.tests.user;

import com.maku.apitest.api.AuthApi;
import com.maku.apitest.api.UserApi;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 修改密码接口测试：PUT /sys/user/password
 * 覆盖：正向（修改专属测试用户密码）、原密码错误、必填缺失（参数化 2 行）、未授权，共 5 个执行用例。
 *
 * ⚠️ 为何不使用 admin 账号测密码修改：
 * - admin 初始密码 "admin" 是弱密码，若 Maku 有密码强度校验，无法通过 API 恢复到 "admin"
 * - admin 密码被改后整个测试套件的 @BeforeAll 登录全部失败（已踩过坑）
 * - 正确做法：@BeforeAll 创建专属测试用户 test_v3_pwd_001，使用其 token 做密码测试
 *
 * 密码恢复策略：
 * - @AfterEach 无条件尝试将密码从 NEW_PASSWORD 改回 TEST_PASSWORD
 * - 若本次测试未改密码（如异常用例、未授权用例），此次 API 调用因"原密码错误"返回 code!=0，忽略响应
 * - 两个密码都是强密码，不存在密码强度导致恢复失败的问题
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserPasswordTest extends BaseTest {

    private UserApi userApi;
    private AuthApi authApi;

    private static final String TEST_USERNAME = "test_v3_pwd_001";
    private static final String TEST_PASSWORD = "TestPwd@v3001!";   // 初始密码
    private static final String NEW_PASSWORD  = "TestPwd@v3002!";   // 改成的密码

    // 专属 token，不使用 admin token，避免改密操作污染全局登录态
    private String pwdTestToken;
    private Long testUserId;
    private Long testOrgId;

    @BeforeAll
    void createPwdTestUser() {
        userApi = new UserApi(specFactory);
        authApi = new AuthApi(specFactory);
        testOrgId = ensureTestOrg();

        // 先清理可能残留的测试用户
        jdbcTemplate.update("DELETE FROM sys_user WHERE username = ?", TEST_USERNAME);

        // 用 admin token 创建测试用户（password 字段由 Maku 服务端哈希存储）
        String saveBody = JsonTemplateUtil.load("template/user/save.json")
                .set("$.username", TEST_USERNAME)
                .set("$.password", TEST_PASSWORD)
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000077")
                .toJson();
        Response saveResp = userApi.save(token, saveBody);
        assertThat((Integer) saveResp.path("code"))
                .as("@BeforeAll: 创建密码测试用户应成功").isEqualTo(0);

        // 查出 id，用于 @AfterAll 删除
        testUserId = jdbcTemplate.queryForObject(
                "SELECT id FROM sys_user WHERE username = ?", Long.class, TEST_USERNAME);

        // 以测试用户身份登录，获取其专属 token
        String loginBody = JsonTemplateUtil.load("template/auth/login.json")
                .set("$.username", TEST_USERNAME)
                .set("$.password", TEST_PASSWORD)
                .toJson();
        pwdTestToken = authApi.login(loginBody).path("data.access_token");
        assertThat(pwdTestToken).as("@BeforeAll: 测试用户应能成功登录").isNotBlank();
    }

    @AfterAll
    void deletePwdTestUser() {
        // 用 admin token 删除专属测试用户
        if (testUserId != null) {
            String deleteBody = JsonTemplateUtil.load("template/user/delete.json")
                    .set("$[0]", testUserId)
                    .toJson();
            userApi.delete(token, deleteBody);
        }
    }

    @AfterEach
    void restorePassword() {
        // 无论上一个测试是否改了密码，都尝试将密码从 NEW_PASSWORD 改回 TEST_PASSWORD。
        // 若密码未被改动，此请求因"原密码错误"返回 code!=0，忽略响应即可。
        String restoreBody = JsonTemplateUtil.load("template/user/password.json")
                .set("$.password", NEW_PASSWORD)
                .set("$.newPassword", TEST_PASSWORD)
                .toJson();
        userApi.updatePassword(pwdTestToken, restoreBody); // 忽略响应
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("修改密码")
    @DisplayName("修改密码-正向：原密码正确，修改成功（@AfterEach 自动恢复）")
    void should_update_password_when_valid() {
        // ① 准备：TEST_PASSWORD → NEW_PASSWORD
        String body = JsonTemplateUtil.load("template/user/password.json")
                .set("$.password", TEST_PASSWORD)
                .set("$.newPassword", NEW_PASSWORD)
                .toJson();

        // ② 调用：使用测试用户专属 token，不影响 admin
        Response response = userApi.updatePassword(pwdTestToken, body);

        // ④ 断言
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(0);
        assertThat((String) response.path("msg")).isEqualTo("success");
        // @AfterEach 会自动将密码恢复为 TEST_PASSWORD
    }

    // ──────────────────────── 原密码错误 ────────────────────────

    @Test
    @Story("修改密码")
    @DisplayName("修改密码-异常：原密码错误，修改失败")
    void should_fail_when_wrong_current_password() {
        // ① 准备：故意写错原密码
        String body = JsonTemplateUtil.load("template/user/password.json")
                .set("$.password", "wrongPassword123!")
                .set("$.newPassword", NEW_PASSWORD)
                .toJson();

        // ② 调用
        Response response = userApi.updatePassword(pwdTestToken, body);

        // ④ 断言：原密码错误应返回 code!=0
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isNotEqualTo(0);
    }

    // ──────────────────────── 必填字段缺失（参数化，纯 delete） ────────────────────────

    /*
     * CSV 列：description, missingField, expectedMsg
     * 先将模板中的 password/newPassword 改为测试用户的值，再 delete 目标字段。
     * expectedMsg 在首次运行后根据实际返回值更新 CSV（有些服务端未做 validation，返回"服务器异常"）。
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/user/password_missing_field.csv", numLinesToSkip = 1)
    @Story("修改密码")
    @DisplayName("修改密码-异常：必填字段缺失，校验不通过")
    void should_fail_when_missing_required_field(
            String description, String missingField, String expectedMsg) {
        // ① 准备：用测试用户凭据替换模板值，再删除目标字段
        String body = JsonTemplateUtil.load("template/user/password.json")
                .set("$.password", TEST_PASSWORD)
                .set("$.newPassword", NEW_PASSWORD)
                .delete("$." + missingField)
                .toJson();

        // ② 调用
        Response response = userApi.updatePassword(pwdTestToken, body);

        // ④ 断言：至少保证 code!=0（msg 由 CSV 提供，首次运行后验证准确性）
        assertThat(response.statusCode()).as(description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as(description).isNotEqualTo(0);
        assertThat((String) response.path("msg")).as(description).isEqualTo(expectedMsg);
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("修改密码")
    @DisplayName("修改密码-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        String body = JsonTemplateUtil.load("template/user/password.json")
                .set("$.password", TEST_PASSWORD)
                .set("$.newPassword", NEW_PASSWORD)
                .toJson();

        Response response = userApi.updatePassword("", body);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
