package com.maku.apitest.tests.user;

import com.maku.apitest.api.UserApi;
import com.maku.apitest.model.common.CommonResp;
import com.maku.apitest.model.common.PageResult;
import com.maku.apitest.model.user.SysUserVO;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 分页查询接口测试：GET /sys/user/page
 * 覆盖：正向（Schema 验证）、按条件过滤、未授权，共 3 个用例（档位 2 基础回归）。
 *
 * 数据准备：
 * - super_admin=1 的 admin 用户被 Maku 从分页列表排除，无法用 username=admin 过滤测试。
 * - @BeforeAll 创建普通测试用户 test_v3_page_001 用于过滤测试，@AfterAll 清理。
 * - sys_org 表初始为空，ensureTestOrg() 插入 test_v3_org 行以满足 orgId 外键（INSERT IGNORE 保证幂等）。
 *
 * GET 请求无 body，参数走 queryParams，JsonTemplateUtil.toMap() 输出 Map。
 * page.json 中 asc=false（boolean 类型，不能是空字符串），gender=null（不传该参数）。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserPageTest extends BaseTest {

    private UserApi userApi;
    private Long testOrgId;

    @BeforeAll
    void setupTestData() {
        userApi = new UserApi(specFactory);
        testOrgId = ensureTestOrg();

        // 清理可能残留的脏数据，再创建过滤测试用的普通用户
        jdbcTemplate.update("DELETE FROM sys_user WHERE username = ?", "test_v3_page_001");
        String saveBody = JsonTemplateUtil.load("template/user/save.json")
                .set("$.username", "test_v3_page_001")
                .set("$.orgId", testOrgId)
                .set("$.mobile", "13800000066")
                .toJson();
        Response saveResp = userApi.save(token, saveBody);
        assertThat((Integer) saveResp.path("code"))
                .as("@BeforeAll: 创建过滤测试用户应成功").isEqualTo(0);
    }

    @AfterAll
    void tearDown() {
        jdbcTemplate.update("DELETE FROM sys_user WHERE username = ?", "test_v3_page_001");
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("分页查询")
    @DisplayName("分页查询-正向：默认参数返回用户列表，结构符合 Schema")
    void should_return_page_when_default_params() {
        // ① 准备：模板默认值（page=1, limit=10），GET 用 toMap()
        Map<String, ?> params = JsonTemplateUtil.load("template/user/page.json").toMap();

        // ② 调用
        Response response = userApi.page(token, params);

        // ③ 解析：分页结果需要多字段断言，用 POJO 反序列化
        CommonResp<PageResult<SysUserVO>> resp =
                response.as(new TypeRef<CommonResp<PageResult<SysUserVO>>>() {});

        // ④ 断言：① HTTP 状态码  ② JSON Schema 结构  ③ 业务字段值
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user/page.json"));
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isNotNull();
        assertThat(resp.getData().getList()).isNotNull();
        assertThat(resp.getData().getTotal()).isGreaterThanOrEqualTo(0);
    }

    // ──────────────────────── 过滤条件 ────────────────────────

    @Test
    @Story("分页查询")
    @DisplayName("分页查询-正向：按 username 过滤，返回匹配 test_v3_page_001 的记录")
    void should_filter_by_username_when_querying() {
        // ① 准备：在模板基础上改 username 过滤条件（admin 是 super_admin，被 Maku 排除在列表外）
        Map<String, ?> params = JsonTemplateUtil.load("template/user/page.json")
                .set("$.username", "test_v3_page_001")
                .toMap();

        // ② 调用
        Response response = userApi.page(token, params);

        // ③ 解析
        CommonResp<PageResult<SysUserVO>> resp =
                response.as(new TypeRef<CommonResp<PageResult<SysUserVO>>>() {});

        // ④ 断言：列表非空且包含目标用户
        response.then().statusCode(200);
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData().getList())
                .isNotEmpty()
                .extracting(SysUserVO::getUsername)
                .contains("test_v3_page_001");
    }

    // ──────────────────────── 过滤无匹配 ────────────────────────

    @Test
    @Story("分页查询")
    @DisplayName("分页查询-正向：username 条件无匹配，返回空列表 total=0")
    void should_return_empty_when_username_not_match() {
        // ① 准备：用一个绝对不存在的 username 过滤
        Map<String, ?> params = JsonTemplateUtil.load("template/user/page.json")
                .set("$.username", "test_v3_no_such_user_xyz")
                .toMap();

        // ② 调用
        Response response = userApi.page(token, params);

        // ③ 解析
        CommonResp<PageResult<SysUserVO>> resp =
                response.as(new TypeRef<CommonResp<PageResult<SysUserVO>>>() {});

        // ④ 断言
        response.then().statusCode(200);
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData().getTotal()).isEqualTo(0);
        assertThat(resp.getData().getList()).isEmpty();
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("分页查询")
    @DisplayName("分页查询-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        // ① 准备
        Map<String, ?> params = JsonTemplateUtil.load("template/user/page.json").toMap();

        // ② 调用：传空字符串模拟未携带 token
        Response response = userApi.page("", params);

        // ④ 断言：MakuBoot 对无效 token 统一返回 HTTP 200 + 业务 code 401
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
