package com.maku.apitest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * 登录响应体 POJO，对应 MakuBoot 统一返回格式：
 *   { "code": 0, "msg": "success", "data": { "access_token": "xxx" } }
 *
 * @JsonProperty 说明：
 * MakuBoot 响应字段名是 access_token（snake_case），Java 惯例是 accessToken（camelCase）。
 * @JsonProperty("access_token") 告诉 Jackson：反序列化时把 JSON 里的 "access_token"
 * 映射到 Java 的 accessToken 字段，序列化时再改回 "access_token"。
 * 注意：jackson-annotations（含 @JsonProperty）是 compile scope，可在 main 代码中使用。
 *
 * 实际测试中更推荐用 GPath：response.path("data.access_token")，
 * LoginResp 保留作为类型安全的备用反序列化方式：response.as(LoginResp.class)
 */
@Data
public class LoginResp {
    private int code;
    private String msg;
    private TokenData data;

    @Data
    public static class TokenData {
        @JsonProperty("access_token")
        private String accessToken;
    }
}
