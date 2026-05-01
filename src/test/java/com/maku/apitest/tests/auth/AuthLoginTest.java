package com.maku.apitest.tests.auth;

import com.maku.apitest.api.AuthApi;
import com.maku.apitest.model.auth.SysTokenVO;
import com.maku.apitest.model.common.CommonResp;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 登录接口测试：/sys/auth/login
 * 覆盖：正向 + 凭据错误 + 必填字段缺失，共 5 个用例（档位 2 基础回归）。
 *
 * CSV 文件按处理逻辑严格分开（CLAUDE.md 约定：一个 CSV = 一种参数处理逻辑）：
 * - login_invalid_credentials.csv → 纯 set，改字段值
 * - login_missing_field.csv       → 纯 delete，删字段
 * 好处：每个测试方法内部无 if-else，逻辑单一清晰。
 *
 * // v1 改进：v1 把"改字段"和"删字段"塞进同一 CSV + if-else 分支，v3 拆开后代码量减半。
 */
@Feature("认证管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthLoginTest extends BaseTest {

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("登录")
    @DisplayName("登录-正向：合法账号密码，返回 access_token")
    void should_return_token_when_valid_credentials() {
        // ① 准备：直接用模板默认值（admin/admin），POST body 用 toJson()
        String body = JsonTemplateUtil.load("template/auth/login.json").toJson();

        // ② 调用
        Response response = new AuthApi(specFactory).login(body);

        // ③ 解析：正向用例要取 token 字段，用 CommonResp<SysTokenVO> 反序列化
        // TypeRef 是 RestAssured 的泛型解析工具，解决 Java 泛型擦除问题
        CommonResp<SysTokenVO> resp = response.as(new TypeRef<CommonResp<SysTokenVO>>() {});

        // ④ 断言：① HTTP 状态码  ② JSON Schema 结构  ③ 业务字段值
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/auth/login.json"));
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getMsg()).isEqualTo("success");
        assertThat(resp.getData().getAccessToken()).isNotBlank();
    }

    // ──────────────────────── 凭据错误（参数化，纯 set） ────────────────────────

    /*
     * CSV 列：description, username, password, expectedMsg
     * 每行只改 username/password，key/captcha 保留模板默认值（测试环境验证码已关闭）。
     * 无论用户名错还是密码错，服务端统一返回"用户名或密码错误"——防账号枚举的安全规范。
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/auth/login_invalid_credentials.csv", numLinesToSkip = 1)
    @Story("登录")
    @DisplayName("登录-异常：凭据错误")
    void should_fail_when_invalid_credentials(
            String description, String username, String password, String expectedMsg) {
        // ① 准备：纯 set，不删字段；key/captcha 由模板兜底
        String body = JsonTemplateUtil.load("template/auth/login.json")
                .set("$.username", username)
                .set("$.password", password)
                .toJson();

        // ② 调用
        Response response = new AuthApi(specFactory).login(body);

        // ③ 省略（异常用例不需要 POJO 反序列化）

        // ④ 断言
        assertThat(response.statusCode()).as(description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as(description).isNotEqualTo(0);
        assertThat((String) response.path("msg")).as(description).isEqualTo(expectedMsg);
    }

    // ──────────────────────── 必填字段缺失（参数化，纯 delete） ────────────────────────

    /*
     * CSV 列：description, missingField, expectedMsg
     * missingField 是 JSON 字段名（如 "username"），delete("$." + missingField) 动态删除。
     * 这样 CSV 加一行就能覆盖一个新的必填字段，测试方法本身不需要改。
     *
     * // v1 改进：v1 用 if(username==null) 逐字段判断，v3 统一 delete("$."+field)，加字段只改 CSV。
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/auth/login_missing_field.csv", numLinesToSkip = 1)
    @Story("登录")
    @DisplayName("登录-异常：必填字段缺失")
    void should_fail_when_missing_required_field(
            String description, String missingField, String expectedMsg) {
        // ① 准备：纯 delete，不改字段值
        String body = JsonTemplateUtil.load("template/auth/login.json")
                .delete("$." + missingField)
                .toJson();

        // ② 调用
        Response response = new AuthApi(specFactory).login(body);

        // ③ 省略

        // ④ 断言
        assertThat(response.statusCode()).as(description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as(description).isNotEqualTo(0);
        assertThat((String) response.path("msg")).as(description).isEqualTo(expectedMsg);
    }
}
