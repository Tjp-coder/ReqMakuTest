package com.maku.apitest.tests.user;

import com.maku.apitest.api.UserApi;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 修改用户接口测试：PUT /sys/user
 * 覆盖：正向（+ DB 验证）、必填字段缺失（参数化 6 行）、未授权，共 8 个执行用例（档位 2 基础回归）。
 *
 * 数据准备策略：
 * - @BeforeAll ensureTestOrg()：确保 sys_org 有测试机构，获取 testOrgId。
 * - @BeforeEach createTestUser()：通过 API 创建测试用户（password 字段需要 Maku 哈希），
 *   再用 JdbcTemplate 查出 id 供 update 请求使用。
 * - @AfterEach cleanup()：JdbcTemplate 按 username 前缀精确删除。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserUpdateTest extends BaseTest {

    private UserApi userApi;
    private Long testOrgId;
    private Long testUserId;

    @BeforeAll
    void initApi() {
        userApi = new UserApi(specFactory);
        testOrgId = ensureTestOrg();
    }

    @BeforeEach
    void createTestUser() {
        jdbcTemplate.update("DELETE FROM sys_user WHERE username LIKE ?", "test_v3_upd_%");

        String saveBody = JsonTemplateUtil.load("template/user/save.json")
                .set("$.username", "test_v3_upd_001")
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000099")
                .toJson();
        Response saveResp = userApi.save(token, saveBody);
        assertThat((Integer) saveResp.path("code"))
                .as("@BeforeEach: 新增测试用户应成功").isEqualTo(0);

        testUserId = jdbcTemplate.queryForObject(
                "SELECT id FROM sys_user WHERE username = ?", Long.class, "test_v3_upd_001");
        assertThat(testUserId).as("@BeforeEach: DB 中应能查到新增的用户 id").isNotNull();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM sys_user WHERE username LIKE ?", "test_v3_upd_%");
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("修改用户")
    @DisplayName("修改用户-正向：合法数据修改成功，DB 可查到修改后的字段")
    void should_update_user_when_valid_data() {
        // ① 准备
        String body = JsonTemplateUtil.load("template/user/update.json")
                .set("$.id", testUserId)
                .set("$.username", "test_v3_upd_001")
                .set("$.orgId", testOrgId)
                .set("$.realName", "修改后姓名")
                .set("$.mobile", "13800000099")
                .toJson();

        // ② 调用
        Response response = userApi.update(token, body);

        // ④ 断言：① HTTP 状态码  ② 业务 code  ③ DB 层验证
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(0);

        String realNameInDb = jdbcTemplate.queryForObject(
                "SELECT real_name FROM sys_user WHERE id = ?", String.class, testUserId);
        assertThat(realNameInDb).as("DB 中 real_name 应已更新").isEqualTo("修改后姓名");
    }

    // ──────────────────────── 必填字段缺失（参数化，纯 delete） ────────────────────────

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/user/update_missing_field.csv", numLinesToSkip = 1)
    @Story("修改用户")
    @DisplayName("修改用户-异常：必填字段缺失，校验不通过")
    void should_fail_when_missing_required_field(
            String description, String missingField, String expectedMsg) {
        // ① 准备：先填入有效 id + orgId，再删除目标字段
        String body = JsonTemplateUtil.load("template/user/update.json")
                .set("$.id", testUserId)
                .set("$.username", "test_v3_upd_001")
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000099")
                .delete("$." + missingField)
                .toJson();

        // ② 调用
        Response response = userApi.update(token, body);

        // ④ 断言
        assertThat(response.statusCode()).as(description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as(description).isNotEqualTo(0);
        assertThat((String) response.path("msg")).as(description).isEqualTo(expectedMsg);
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("修改用户")
    @DisplayName("修改用户-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        String body = JsonTemplateUtil.load("template/user/update.json")
                .set("$.id", testUserId)
                .set("$.username", "test_v3_upd_001")
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000099")
                .toJson();

        Response response = userApi.update("", body);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
