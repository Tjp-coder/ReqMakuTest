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
 * 一个 Test 类只测一个接口（v3 约定），覆盖正向 + 5 种异常。
 *
 * 测试方法严格四段式：
 * ① 准备（JsonTemplateUtil 加载/修改模板，POST body 用 toJson()）
 * ② 调用（AuthApi.login 返回 Response）
 * ③ 解析（正向用例用 CommonResp<SysTokenVO>；异常用例省略此步）
 * ④ 断言（HTTP 状态码 + 业务 code/msg + 关键字段）
 *
 * // v1 改进：v1 直接用 Map<String,String> 拼请求体，字段名拼错只在运行时发现。
 * //          v3 用 JsonTemplateUtil 读 JSON 模板，toJson() 输出 POST body，更规范。
 */
@Feature("认证管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthLoginTest extends BaseTest {

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("登录")
    @DisplayName("登录-正向：合法账号密码，返回 access_token")
    void should_return_token_when_valid_credentials() {
        // ① 准备：直接用模板默认值（admin/admin）；POST body 用 toJson()
        String body = JsonTemplateUtil.load("template/auth/login.json").toJson();

        // ② 调用
        Response response = new AuthApi(specFactory).login(body);

        // ③ 解析：正向用例要验证 token 字段，用 CommonResp<SysTokenVO> 反序列化
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

    // ──────────────────────── 异常用例（参数化） ────────────────────────

    /*
     * @ParameterizedTest + @CsvFileSource 知识点（第一次出现）：
     * - @ParameterizedTest：同一个测试方法按不同参数跑多次。
     * - @CsvFileSource：从 CSV 文件读取参数，numLinesToSkip=1 跳过表头行。
     * - 方法参数对应 CSV 每列，由 JUnit 5 自动注入；空值（,,）注入为 null。
     *
     * // v1 继承：参数化 + CSV 这套用法从 v1 直接搬来，是最好用的特性之一。
     *
     * null 处理策略：
     * - null → delete（字段完全消失，对应"必填项缺失"场景）
     * - 非 null → set（改成指定值，对应"错误值"场景）
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvFileSource(resources = "/params/auth/falselogin.csv", numLinesToSkip = 1)
    @Story("登录")
    @DisplayName("登录-异常：错误凭据应返回业务错误")
    void should_return_error_when_wrong_credentials(String description, String username, String password) {
        // ① 准备：按场景修改模板字段；POST body 用 toJson()
        JsonTemplateUtil tpl = JsonTemplateUtil.load("template/auth/login.json");
        if (username == null) tpl.delete("$.username"); else tpl.set("$.username", username);
        if (password == null) tpl.delete("$.password"); else tpl.set("$.password", password);
        String body = tpl.toJson();

        // ② 调用
        Response response = new AuthApi(specFactory).login(body);

        // ③ 省略：异常用例只断言 code/msg/data，无需反序列化为 POJO

        // ④ 断言
        assertThat(response.statusCode()).as("HTTP 状态码 - %s", description).isEqualTo(200);
        assertThat((Integer) response.path("code")).as("业务 code - %s", description).isEqualTo(500);
        assertThat((String) response.path("msg")).as("业务 msg - %s", description).isEqualTo("用户名或密码错误");
        assertThat((Object) response.path("data")).as("data 应为 null - %s", description).isNull();
    }
}
