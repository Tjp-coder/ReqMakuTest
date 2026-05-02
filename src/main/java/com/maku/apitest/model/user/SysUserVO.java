package com.maku.apitest.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 对应 MakuBoot /sys/user/{id} 和 /sys/user/info 响应的 data 字段。
 * DB 列名是 snake_case（real_name / org_id），Spring 默认序列化为 camelCase（realName / orgId），
 * 无需 @JsonProperty 映射，字段名与 JSON key 直接对应。
 *
 * // v1 改进：v1 没有响应 POJO，直接用 JsonPath 字符串取字段（如 "$.data.realName"）。
 * //          v3 用 POJO 反序列化，IDE 可以类型安全地 getXxx()，重构时字段名改错立刻报编译错误。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserVO {
    private Long id;
    private String username;
    private String realName;
    private String avatar;
    private Integer gender;
    private String email;
    private String mobile;
    private Long orgId;
    private String orgName;
    private Integer superAdmin;
    private Integer status;
    private List<Long> roleIdList;
    private List<Long> postIdList;
    private String createTime;
}
