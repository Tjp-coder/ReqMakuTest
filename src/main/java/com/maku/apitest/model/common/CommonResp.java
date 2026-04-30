package com.maku.apitest.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/*
 * 通用响应壳，对应 MakuBoot 所有接口的统一返回格式：{ "code": 0, "msg": "success", "data": ... }
 *
 * 泛型 T 是 data 字段的实际类型，例如：
 *   CommonResp<SysTokenVO>        ← 登录响应
 *   CommonResp<PageResult<SysUserVO>>  ← 分页查询响应
 *   CommonResp<Void>              ← 只有 code/msg 的响应（如登出）
 *
 * 用法示例：
 *   CommonResp<SysTokenVO> resp = response.as(new TypeRef<CommonResp<SysTokenVO>>() {});
 *   assertThat(resp.getData().getAccessToken()).isNotBlank();
 *
 * @JsonIgnoreProperties(ignoreUnknown = true)：
 * 接口新增字段时 POJO 不必同步修改，防止 Jackson 因未知字段抛出反序列化异常。
 * 这是所有响应 POJO 的必备注解。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResp<T> {
    private Integer code;
    private String msg;
    private T data;
}
