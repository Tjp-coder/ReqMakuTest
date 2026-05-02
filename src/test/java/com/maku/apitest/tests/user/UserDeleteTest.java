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

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 删除用户接口测试：DELETE /sys/user
 * 覆盖：正向（+ DB 验证删除生效）、未授权，共 2 个用例（档位 2 基础回归）。
 *
 * DELETE /sys/user body 为 JSON 数组，如 [123]，template/user/delete.json 默认为 [1]，
 * 测试前通过 set("$[0]", testUserId) 替换为真实 id。
 *
 * 不测"删除不存在的 id"：Maku 对此通常返回 code=0（幂等），不构成有意义的异常场景。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDeleteTest extends BaseTest {

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
        jdbcTemplate.update("DELETE FROM sys_user WHERE username LIKE ?", "test_v3_del_%");

        String saveBody = JsonTemplateUtil.load("template/user/save.json")
                .set("$.username", "test_v3_del_001")
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000088")
                .toJson();
        Response saveResp = userApi.save(token, saveBody);
        assertThat((Integer) saveResp.path("code"))
                .as("@BeforeEach: 新增被删除目标用户应成功").isEqualTo(0);

        testUserId = jdbcTemplate.queryForObject(
                "SELECT id FROM sys_user WHERE username = ?", Long.class, "test_v3_del_001");
        assertThat(testUserId).as("@BeforeEach: DB 中应能查到新增的用户 id").isNotNull();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM sys_user WHERE username LIKE ?", "test_v3_del_%");
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("删除用户")
    @DisplayName("删除用户-正向：合法 id 删除成功，DB 中该用户不存在")
    void should_delete_user_when_valid_id() {
        // ① 准备：将模板中占位 id 替换为真实 testUserId
        String body = JsonTemplateUtil.load("template/user/delete.json")
                .set("$[0]", testUserId)
                .toJson();

        // ② 调用
        Response response = userApi.delete(token, body);

        // ④ 断言：① HTTP 状态码  ② 业务 code  ③ DB 验证
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(0);

        // Maku 采用软删除（deleted=1），查 deleted=0 的记录应为 0
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE id = ? AND deleted = 0", Integer.class, testUserId);
        assertThat(count).as("Maku 软删除后 deleted=0 的记录应不存在").isEqualTo(0);
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("删除用户")
    @DisplayName("删除用户-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        String body = JsonTemplateUtil.load("template/user/delete.json")
                .set("$[0]", testUserId)
                .toJson();

        Response response = userApi.delete("", body);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
