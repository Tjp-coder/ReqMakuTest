package com.maku.apitest.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * 对应 MakuBoot /sys/auth/login 响应的 data 字段。
 * 完整响应结构：CommonResp<SysTokenVO>
 * 命名直接复用 Maku 后端的 VO 名，放在 model/auth/ 目录下，方便按模块查找。
 *
 * 为什么建这个 POJO（符合 CLAUDE.md 响应侧按需建 POJO 原则）：
 * - BaseTest.loginAndGetToken() 提取 token 后缓存到 this.token，供所有子类使用
 * - token 会跨用例传递，类型安全比 GPath 字符串提取更可靠
 * - 且登录接口在每个测试类的 @BeforeAll 都会被调用，值得封装清楚
 *
 * @JsonProperty("access_token")：
 * 后端 JSON 字段名是 snake_case（access_token），Java 惯例是 camelCase（accessToken）。
 * 此注解告诉 Jackson 反序列化时做名字映射。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysTokenVO {
    @JsonProperty("access_token")
    private String accessToken;
}
