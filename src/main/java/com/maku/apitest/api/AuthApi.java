package com.maku.apitest.api;

import com.maku.apitest.client.RequestSpecFactory;
import com.maku.apitest.config.Env;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.restassured.response.Response;

/*
 * 认证模块 API 封装，对应 MakuBoot 的 /sys/auth/* 接口。
 *
 * 设计原则：
 * - 测试类只做"调用 + 断言"，不知道 URL、请求头、序列化细节。
 * - login() 接收 Map<String, ?> 而非请求 POJO：
 *   请求侧统一用 JsonTemplateUtil + 模板构造 Map，
 *   异常用例可灵活删字段（必填项缺失）或改字段（错误值），无需为每种变体建一个 POJO。
 *
 * // v1 改进：v1 的 Auth.java 继承 BaseInterface，login() 把 token 写入 static 字段，
 * //          多线程时类 A 的 login 会覆盖类 B 正在用的 token，导致 401。
 * //          v3 直接 return Response，调用方自己取 token 存到实例字段，并发安全。
 */
public class AuthApi extends BaseApi {

    public AuthApi(RequestSpecFactory factory) {
        super(factory);
    }

    /**
     * 登录接口，接收由 JsonTemplateUtil.toJson() 构造的 JSON 字符串请求体。
     * POST 请求体用 toJson()，确保 Content-Type: application/json 正确传递。
     * 返回原始 Response，调用方自行做断言或提取数据。
     */
    public Response login(String body) {
        return unauthReq()
                .body(body)
                .when()
                .post("/sys/auth/login")
                .then()
                .extract().response();
    }

    /**
     * 登录并返回 access_token，专供 BaseTest.login() 使用。
     * 内部用 JsonTemplateUtil 加载模板并以 toJson() 输出请求体（POST body 规范）。
     *
     * 为什么 loginAndGetToken 自己加载模板而非让 BaseTest 准备 body：
     * BaseTest 不应关心请求体的构造细节（那是 API 层的职责）。
     */
    public String loginAndGetToken(Env env) {
        String body = JsonTemplateUtil.load("template/auth/login.json")
                .set("$.username", env.getAuthUsername())
                .set("$.password", env.getAuthPassword())
                .toJson();
        String token = login(body).path("data.access_token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "登录失败：未能获取 access_token，请检查 test.properties 中的 authUsername/authPassword");
        }
        return token;
    }

    /**
     * 登出接口，使用 req(token) 自动带上 Authorization Header。
     */
    public Response logout(String token) {
        return req(token)
                .when()
                .post("/sys/auth/logout")
                .then()
                .extract().response();
    }
}
