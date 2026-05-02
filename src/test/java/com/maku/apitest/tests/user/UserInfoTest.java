package com.maku.apitest.tests.user;

import com.maku.apitest.api.UserApi;
import com.maku.apitest.model.common.CommonResp;
import com.maku.apitest.model.user.SysUserVO;
import com.maku.apitest.tests.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 当前登录用户信息接口测试：GET /sys/user/info
 * 覆盖：正向（验证返回的是 admin 账号）、未授权，共 2 个用例（档位 2 基础回归）。
 *
 * /sys/user/info 无请求参数，不需要模板，跳过 ① 准备步骤。
 * 响应字段与 /sys/user/{id} 结构一致，复用 SysUserVO + schemas/user/info.json。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserInfoTest extends BaseTest {

    private UserApi userApi;

    @BeforeAll
    void initApi() {
        userApi = new UserApi(specFactory);
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("当前用户信息")
    @DisplayName("当前用户信息-正向：用 admin token 查询，返回 admin 的用户信息")
    void should_return_current_user_info() {
        // ① 准备：无请求参数，跳过

        // ② 调用
        Response response = userApi.info(token);

        // ③ 解析：需要验证 username 字段，用 POJO 反序列化
        CommonResp<SysUserVO> resp = response.as(new TypeRef<CommonResp<SysUserVO>>() {});

        // ④ 断言：① HTTP 状态码  ② JSON Schema 结构  ③ 业务字段
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user/info.json"));
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isNotNull();
        // 当前用 BaseTest 的 admin token 登录，info 应该返回 admin 自己的信息
        assertThat(resp.getData().getUsername()).isEqualTo(env.getAuthUsername());
    }

    // ──────────────────────── superAdmin 字段验证 ────────────────────────

    @Test
    @Story("当前用户信息")
    @DisplayName("当前用户信息-正向：admin 的 superAdmin 字段为 1（超级管理员标识）")
    void should_return_super_admin_flag_for_admin() {
        // ② 调用
        Response response = userApi.info(token);

        // ③ 解析
        CommonResp<SysUserVO> resp = response.as(new TypeRef<CommonResp<SysUserVO>>() {});

        // ④ 断言
        response.then().statusCode(200);
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData().getSuperAdmin()).isEqualTo(1);
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("当前用户信息")
    @DisplayName("当前用户信息-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        // ② 调用：传空字符串模拟未携带 token
        Response response = userApi.info("");

        // ④ 断言
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
