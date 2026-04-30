package com.maku.apitest.tests.auth;

import com.maku.apitest.api.AuthApi;
import com.maku.apitest.tests.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 登出接口测试：/sys/auth/logout
 *
 * 注意 token 使用策略：
 * - 正向用例：不直接消耗 BaseTest 的 this.token，
 *   而是临时登录一次拿到 freshToken 再登出。
 *   原因：若正向用例让 BaseTest token 失效，后续其他测试类的 @BeforeAll
 *         不会重新登录（PER_CLASS 只执行一次），导致用到同一 token 的测试失败。
 *   这是一个常见的并发 / 顺序陷阱。// v1 改进：v1 在 AuthTest 里登出后就没有可用 token 了。
 * - 异常用例（无 token）：直接传空字符串即可，不涉及任何真实 token。
 */
@Feature("认证管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthLogoutTest extends BaseTest {

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("登出")
    @DisplayName("登出-正向：有效 token 登出成功")
    void should_return_success_when_valid_token() {
        // 单独登录拿一个"用完即弃"的 token，不影响 BaseTest 缓存的 this.token
        String freshToken = new AuthApi(specFactory).loginAndGetToken(env);

        var response = new AuthApi(specFactory).logout(freshToken);

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
        /*
         * 传入空字符串模拟"未携带 token"。
         * MakuBoot 对没有有效 Authorization Header 的请求返回 HTTP 200 + code 401。
         * 注意：这里 HTTP 状态码仍是 200，业务 code 才是 401（统一返回格式的特点）。
         * // v1 经验：v1 AuthTest.test02_false01() 已验证这个行为，v3 直接复用结论。
         */
        var response = new AuthApi(specFactory).logout("");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
        assertThat((Object) response.path("data")).isNull();
    }
}
