package com.maku.apitest.api;

import com.maku.apitest.client.RequestSpecFactory;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

/*
 * 用户管理模块 API 封装，对应 MakuBoot 的 /sys/user/* 接口（共 9 个）。
 *
 * 参数类型规范（CLAUDE.md 强约束，违反会导致序列化问题）：
 * - POST / PUT / DELETE body：接 String（由测试代码的 JsonTemplateUtil.toJson() 生成）
 *   原因：toJson() 输出标准 JSON 字符串，ContentType:JSON 下不会被误当 form 参数处理。
 * - GET query 参数：接 Map（由 JsonTemplateUtil.toMap() 生成），经 queryParams() 发送。
 * - 路径参数（id）：接 Long，由 RestAssured 自动拼入 URL 模板 {id}。
 *
 * // v1 改进：v1 UserApi.save() 接 DocumentContext（整个 JsonPath 上下文），与模板解析强耦合；
 * //          v3 改为接 String，API 类不感知模板，测试类负责构造 body，职责分离更清晰。
 */
public class UserApi extends BaseApi {

    public UserApi(RequestSpecFactory factory) {
        super(factory);
    }

    /** GET /sys/user/page — 分页查询。toMap() 生成 Map，经 queryParams() 作为 query string 发送。*/
    public Response page(String token, Map<String, ?> params) {
        return req(token)
                .queryParams(params)
                .when()
                .get("/sys/user/page");
    }

    /** GET /sys/user/{id} — 按 ID 查询用户详情。RestAssured 将 Long 自动转为路径字符串。*/
    public Response getById(String token, Long id) {
        return req(token)
                .when()
                .get("/sys/user/{id}", id);
    }

    /** GET /sys/user/info — 当前登录用户信息，不需要任何参数，直接发请求。*/
    public Response info(String token) {
        return req(token)
                .when()
                .get("/sys/user/info");
    }

    /** POST /sys/user — 新增用户。body 由 JsonTemplateUtil.toJson() 构造，Content-Type: JSON。*/
    public Response save(String token, String body) {
        return req(token)
                .body(body)
                .when()
                .post("/sys/user");
    }

    /**
     * PUT /sys/user — 修改用户。body 由 JsonTemplateUtil.toJson() 构造。
     * 注意：MakuBoot 修改用户用 PUT，v1 误用了 POST（已在 v3 修正）。
     */
    public Response update(String token, String body) {
        return req(token)
                .body(body)
                .when()
                .put("/sys/user");
    }

    /**
     * DELETE /sys/user — 删除用户（支持批量），body 为 JSON 数组，如 [1, 2, 3]。
     * 测试代码用 JsonTemplateUtil.load("template/user/delete.json")
     *     .set("$[0]", id).toJson() 构造单个 id 的数组体。
     */
    public Response delete(String token, String body) {
        return req(token)
                .body(body)
                .when()
                .delete("/sys/user");
    }

    /** PUT /sys/user/password — 修改当前登录用户密码。password=原密码, newPassword=新密码。*/
    public Response updatePassword(String token, String body) {
        return req(token)
                .body(body)
                .when()
                .put("/sys/user/password");
    }

    /** GET /sys/user/export — 导出用户 Excel。返回字节流，本阶段不建测试类（超出档位 2）。*/
    public Response export(String token, Map<String, ?> params) {
        return req(token)
                .queryParams(params)
                .when()
                .get("/sys/user/export");
    }

    /** POST /sys/user/import — 导入用户 Excel（multipart）。本阶段不建测试类（超出档位 2）。*/
    public Response importUsers(String token, File file) {
        return req(token)
                .multiPart("file", file)
                .contentType("multipart/form-data")
                .when()
                .post("/sys/user/import");
    }
}
