package com.maku.apitest.client;

import com.maku.apitest.config.Env;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.PrintStream;

import static io.restassured.RestAssured.given;

/*
 * 职责：集中管理所有 HTTP 请求的公共配置，是整个框架中所有请求的唯一入口
 *
 * 核心概念：
 * - RequestSpecification：RestAssured 的"预配置请求构建器"。
 *   可以把 baseUri/Header/ContentType 等公共配置预先打包好，
 *   后续每次发请求只需在它基础上追加本次特有的参数（路径、body、queryParam）。
 * - RequestSpecBuilder：构建不可变的 RequestSpecification。
 *   build() 之后的对象是快照，后续修改不影响它，可以安全复用为基础模板。
 * - Filter：RestAssured 的拦截器接口，每次请求/响应都会经过。
 *   这里挂载了日志 Filter 和 Allure 附件 Filter。
 *
 * // v1 改进：v1 在 BaseTest 里直接 `RestAssured.baseURI = "..."` 修改全局静态变量。
 * //          多测试类并发运行时，一个类的 setAll() 会覆盖另一个类的配置，导致 401/404。
 * //          v3 每次调用 unauth()/auth() 都返回新的 RequestSpecification 实例，互不干扰。
 */
public class RequestSpecFactory {

    // 基础快照：包含 baseUri/port/ContentType 和三个 Filter，不含 token。
    // 作为所有请求的公共基础，unauth() 和 auth() 都在它之上叠加各自特有的配置。
    private final RequestSpecification baseSpec;

    //知识扩展:final 字段在多线程下还有个隐藏好处——JMM(Java 内存模型)保证构造函数结束后,其他线程能立刻看到 final 字段的最终值。普通字段没这个保证。所以 final 字段多线程读是安全的。

    /*
     * 为什么用 PrintStream.nullOutputStream()：
     * RequestLoggingFilter/ResponseLoggingFilter 默认打到 System.out，开发期有用，
     * 但在 CI 里会淹没有效信息。这里输出到 /dev/null，转而依赖 AllureRestAssured 将
     * 请求/响应保存到报告附件，失败时在报告里查看，比控制台更方便。
     * 如需开启控制台日志，把 nullOutputStream() 改为 System.out 即可。
     */
    public RequestSpecFactory(Env env) {
        PrintStream silent = new PrintStream(PrintStream.nullOutputStream());
        this.baseSpec = new RequestSpecBuilder()
                .setBaseUri(env.getBaseUrl())
                .setPort(env.getPort())
                .setContentType(ContentType.JSON)
                // Allure 附件 Filter：每次请求后自动把 curl 命令和响应体写入 Allure 报告
                .addFilter(new AllureRestAssured())
                // 日志 Filter：输出到 silent 流（静默），失败排查靠 Allure 报告
                .addFilter(new RequestLoggingFilter(LogDetail.ALL, silent))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL, silent))
                .build();
    }

    /**
     * 无鉴权规格，用于登录、公开接口等不需要 token 的请求。
     * 每次调用返回新实例，并发安全。
     */
    public RequestSpecification unauth() {
        return given().spec(baseSpec);
    }

    /**
     * 带鉴权规格，追加 Authorization Header。
     * MakuBoot 的鉴权方式：Header Key = "Authorization"，Value = access_token（无 "Bearer " 前缀）。
     *
     * // v1 改进：v1 用 static String token 全局共享，多类并发时相互污染；
     * //          v3 通过参数传入，每个测试类自己持有 token 实例字段。
     */
    public RequestSpecification auth(String token) {
        return given().spec(baseSpec).header("Authorization", token);
    }
}
