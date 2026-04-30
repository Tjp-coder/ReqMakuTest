package com.maku.apitest.tests.auth;

import com.maku.apitest.api.AuthApi;
import com.maku.apitest.tests.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 登出接口测试：/sys/auth/logout
 * 登出没有请求体，跳过 ① 准备步骤；只断言 code/msg/data，跳过 ③ 解析步骤。
 *
 * token 使用策略（重要）：
 * - 正向用例：临时登录拿 freshToken 再登出，不消耗 BaseTest 缓存的 this.token。
 *   原因：若登出消耗了 this.token，后续同一测试类中其他用例将因 token 失效而 401。
 *   // v1 改进：v1 AuthTest 登出后整个类的 token 就失效了，后续用例全挂。
 * - 异常用例（无 token）：传空字符串，不涉及任何真实 token。
 */
@Feature("认证管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthLogoutTest extends BaseTest {

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("登出")
    @DisplayName("登出-正向：有效 token 登出成功")
    void should_return_success_when_valid_token() {
        // ① 准备：临时登录拿一个"用完即弃"的 freshToken
        String freshToken = new AuthApi(specFactory).loginAndGetToken(env);

        // ② 调用
        Response response = new AuthApi(specFactory).logout(freshToken);

        // ③ 省略：登出响应只有 code/msg，data 为 null，无需 POJO 解析

        // ④ 断言
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(0);
        assertThat((String) response.path("msg")).isEqualTo("success");
        assertThat((Object) response.path("data")).isNull();
    }

    // ──────────────────────── 异常用例 ────────────────────────

    @Test
    @Story("登出")
    @DisplayName("登出-异常：无 token，应返回 401 未授权")
    void should_return_401_when_no_token() {
        // ① 准备：无（无 token 场景不需要任何准备）

        // ② 调用：传空字符串模拟未携带 token
        Response response = new AuthApi(specFactory).logout("");

        // ③ 省略

        // ④ 断言
        // MakuBoot 对无效 Authorization 的请求：HTTP 200 + 业务 code 401（统一返回格式特点）
        // // v1 经验：v1 AuthTest.test02_false01() 已验证这个行为，v3 直接复用结论。
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
        assertThat((Object) response.path("data")).isNull();
    }
}
