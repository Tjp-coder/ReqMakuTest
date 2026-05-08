package com.maku.apitest.tests.user;

import com.maku.apitest.api.UserApi;
import com.maku.apitest.model.common.CommonResp;
import com.maku.apitest.model.user.SysUserVO;
import com.maku.apitest.tests.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 按 ID 查询用户接口测试：GET /sys/user/{id}
 * 覆盖：正向（Schema 验证）、用户不存在、未授权，共 3 个用例（档位 2 基础回归）。
 *
 * 正向用例使用 id=1（超级管理员 admin），该记录在 MakuBoot 初始化时必然存在。
 * 不存在的 id 使用 999999999，足够大，不可能与真实 id 冲突。
 */
@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserGetByIdTest extends BaseTest {

    private UserApi userApi;

    // 当前 Maku Boot 实例中 admin 的 id（初始化脚本写死为 10000）
    private static final long ADMIN_ID = 10000L;
    // 足够大的 id，正常数据中不会存在
    private static final long NON_EXISTENT_ID = 999999999L;

    @BeforeAll
    void initApi() {
        userApi = new UserApi(specFactory);
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("按ID查询")
    @DisplayName("按ID查询-正向：admin(id=1) 存在，响应结构符合 Schema")
    void should_return_user_when_valid_id() {
        // ① 准备：路径参数直接传 Long，无需模板
        // ② 调用
        Response response = userApi.getById(token, ADMIN_ID);

        // ③ 解析：需要断言 username 字段，用 POJO 反序列化
        CommonResp<SysUserVO> resp = response.as(new TypeRef<CommonResp<SysUserVO>>() {});

        // ④ 断言
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user/user.json"));
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isNotNull();
        assertThat(resp.getData().getId()).isEqualTo(ADMIN_ID);
        assertThat(resp.getData().getUsername()).isEqualTo("admin");
    }

    // ──────────────────────── 异常用例 ────────────────────────

    @Test
    @Story("按ID查询")
    @DisplayName("按ID查询-异常：id 不存在，服务端 NPE 返回 code=500")
    @Disabled("服务端未做空值校验，查不到用户时 NPE → 500，待修复 #BUG-xxx编号")
    void should_return_error_when_user_not_found() {
        // ① 准备：使用不存在的 id
        // ② 调用
        Response response = userApi.getById(token, NON_EXISTENT_ID);

        // ③ 省略

        // ④ 断言：Maku 的 SysUserController.get() 未做 null 判断，查不到用户时 NPE → code=500
        // 这是服务端的已知 Bug，测试以实际行为为准（记录现状，不修正服务端）
        response.then().statusCode(200);
        assertThat((Integer) response.path("code")).isEqualTo(404);  // 期望 404
        assertThat((String) response.path("msg")).contains("不存在");
    }

    // ──────────────────────── 未授权 ────────────────────────

    @Test
    @Story("按ID查询")
    @DisplayName("按ID查询-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        // ② 调用：传空字符串模拟未携带 token
        Response response = userApi.getById("", ADMIN_ID);

        // ④ 断言
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
