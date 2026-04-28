package com.maku.apitest.tests;

/*
 * 职责：Phase 1 骨架验收测试，验证 Env 和 JsonTemplateUtil 能正常初始化。
 *       不继承 BaseTest，避免触发登录（Phase 1 没有真实服务器）。
 *       Phase 2 完成后可以删除此文件。
 */
import com.maku.apitest.config.Env;
import com.maku.apitest.utils.JsonTemplateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {

    @Test
    @DisplayName("骨架验收-Env 能加载到配置（baseUrl 非空，port > 0）")
    void env_loads_correctly() {
        Env env = Env.get();
        // AssertJ 的链式断言：比 Hamcrest 更接近自然语言
        assertThat(env.getBaseUrl())
                .as("baseUrl 不应为空")
                .isNotBlank();
        assertThat(env.getPort())
                .as("port 应为有效端口号")
                .isGreaterThan(0)
                .isLessThanOrEqualTo(65535);
    }

    @Test
    @DisplayName("骨架验收-JsonTemplateUtil 能加载模板并修改字段")
    void json_template_loads_and_modifies_correctly() {
        // 使用 v1 归档的模板文件验证 JsonTemplateUtil 能正常工作
        String json = JsonTemplateUtil.load("v1/template/auth/login.json")
                .set("$.username", "smoke_test_user")
                .toJson();

        assertThat(json)
                .as("修改后的 JSON 应包含新 username")
                .contains("smoke_test_user")
                // 只验证 username 已被改写，不断言整体无 admin（password 字段值本就是 admin）
                .contains("\"username\":\"smoke_test_user\"");
    }
}
