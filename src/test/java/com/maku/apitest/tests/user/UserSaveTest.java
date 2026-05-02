package com.maku.apitest.tests.user;

import com.maku.apitest.api.UserApi;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 新增用户接口测试：POST /sys/user
 * 覆盖：正向（+ DB 验证）、必填字段缺失（参数化 6 行）、未授权，共 8 个执行用例（档位 2 基础回归）。
 *
 * 数据准备：
 * - sys_org 初始为空，@BeforeAll 通过 ensureTestOrg() 插入 test_v3_org，获取 testOrgId。
 * - 所有需要合法 orgId 的测试用 .set("$.orgId", testOrgId) 覆盖模板的占位值。
 * - @AfterEach 按 username 前缀 LIKE 'test_v3_save_%' 精确删除测试数据。
 *
 * // v1 改进：v1 在测试方法内 DELETE FROM sys_user（全表删除），v3 改为精确前缀匹配。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserSaveTest extends BaseTest {

    private UserApi userApi;
    private Long testOrgId;

    @BeforeAll
    void initApi() {
        userApi = new UserApi(specFactory);
        testOrgId = ensureTestOrg();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM sys_user WHERE username LIKE ?", "test_v3_save_%");
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("新增用户")
    @DisplayName("新增用户-正向：合法数据保存成功，DB 可查到该记录")
    void should_save_user_when_valid_data() {
        // ① 准备：模板默认值 + 覆盖 orgId 为有效机构 id，POST body 用 toJson()
        String body = JsonTemplateUtil.load("template/user/save.json")
                .set("$.orgId", testOrgId)
                .toJson();

        // ② 调用
        Response response = userApi.save(token, body);

        // ③ 省略：POST /sys/user 成功时 data 为 null，无需 POJO 解析

        // ④ 断言：① HTTP 状态码  ② 业务 code  ③ DB 层验证
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(0);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE username = ?",
                Integer.class, "test_v3_save_001");
        assertThat(count).as("DB 中应能查到刚新增的用户").isEqualTo(1);
    }

    // ──────────────────────── 必填字段缺失（参数化，纯 delete） ────────────────────────

    /*
     * CSV 列：description, missingField, expectedMsg
     * 每行测一个必填字段缺失场景。
     * 先 set 有效 orgId，再 delete 目标字段：当 missingField=orgId 时，set 后立即 delete，最终效果是 orgId 缺失。
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/user/save_missing_field.csv", numLinesToSkip = 1)
    @Story("新增用户")
    @DisplayName("新增用户-异常：必填字段缺失，校验不通过")
    void should_fail_when_missing_required_field(
            String description, String missingField, String expectedMsg) {
        // ① 准备：先填入有效 orgId（避免因 orgId 无效干扰其他字段的校验），再删除要测试的字段
        String body = JsonTemplateUtil.load("template/user/save.json")
                .set("$.orgId", testOrgId)
                .delete("$." + missingField)
                .toJson();

        // ② 调用
        Response response = userApi.save(token, body);

        // ④ 断言
        assertThat(response.statusCode()).as(description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as(description).isNotEqualTo(0);
        assertThat((String) response.path("msg")).as(description).isEqualTo(expectedMsg);
    }

    // ──────────────────────── 重复用户名 ────────────────────────

    @Test
    @Story("新增用户")
    @DisplayName("新增用户-异常：用户名重复，保存失败")
    void should_fail_when_duplicate_username() {
        // ① 准备：先保存一次
        String body = JsonTemplateUtil.load("template/user/save.json")
                .set("$.orgId", testOrgId)
                .toJson();
        userApi.save(token, body); // 第一次保存，成功

        // ② 调用：相同 username 再次保存
        Response response = userApi.save(token, body);

        // ④ 断言：username 唯一约束冲突，code 应非 0
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isNotEqualTo(0);
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("新增用户")
    @DisplayName("新增用户-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        // auth 检查在业务逻辑之前，orgId 是否有效不影响鉴权结果
        String body = JsonTemplateUtil.load("template/user/save.json").toJson();

        Response response = userApi.save("", body);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
