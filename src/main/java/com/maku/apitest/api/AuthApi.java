package com.maku.apitest.api;

import com.maku.apitest.client.RequestSpecFactory;
import com.maku.apitest.config.Env;
import com.maku.apitest.model.LoginReq;
import io.restassured.response.Response;

/*
 * 认证模块 API 封装，对应 MakuBoot 的 /sys/auth/* 接口。
 *
 * 设计原则：
 * - 测试类只做"调用 + 断言"，不知道 URL、请求头、序列化细节。
 * - AuthApi 封装这些细节，测试类调 login()/logout() 就够了。
 * - loginAndGetToken() 是 BaseTest 专用的便捷方法，避免 BaseTest 直接操作 Response。
 *
 * // v1 改进：v1 的 Auth.java 继承 BaseInterface，login() 把 token 写入 BaseInterface.token 静态字段，
 * //          多线程时一个测试类的 login 会覆盖另一个类的 token。
 * //          v3 直接 return Response，调用方自己从 Response 中取 token 存到实例字段，并发安全。
 */
public class AuthApi extends BaseApi {

    public AuthApi(RequestSpecFactory factory) {
        super(factory);
    }

    /**
     * 登录接口。
     * 使用 LoginReq POJO，由 RestAssured + Jackson 自动序列化为 JSON。
     * 返回原始 Response，让调用方自己做断言或提取数据。
     */
    public Response login(String username, String password) {
        return unauthReq()
                .body(LoginReq.builder().username(username).password(password).build())
                .when()
                .post("/sys/auth/login")
                .then()
                .extract().response();
    }

    /**
     * 登录并直接返回 access_token，专供 BaseTest.login() 使用。
     * 用 GPath（RestAssured 内置）提取嵌套字段，比手写 JSON 解析更简洁。
     *
     * GPath 知识点：
     * response.path("data.access_token") 等价于：
     *   JsonPath.from(response.asString()).getString("data.access_token")
     * 区别：path() 是 RestAssured 封装，更简短；两者底层 GPath 表达式语法一致。
     */
    public String loginAndGetToken(Env env) {
        String token = login(env.getAuthUsername(), env.getAuthPassword())
                .path("data.access_token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "登录失败：未能获取 access_token，请检查 test.properties 中的 authUsername/authPassword");
        }
        return token;
    }

    /**
     * 登出接口。
     * 使用 req(token) 自动带上 Authorization Header，对应 MakuBoot 鉴权方式。
     */
    public Response logout(String token) {
        return req(token)
                .when()
                .post("/sys/auth/logout")
                .then()
                .extract().response();
    }
}
