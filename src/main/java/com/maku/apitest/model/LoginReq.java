package com.maku.apitest.model;

import lombok.Builder;
import lombok.Data;

/*
 * 登录请求体 POJO。
 *
 * Lombok 注解说明（第一次出现，重点解释）：
 * - @Data：自动生成 getter、setter、toString、equals、hashCode，消除大量样板代码。
 * - @Builder：自动生成构建者模式，允许链式创建对象：
 *     LoginReq.builder().username("admin").password("admin").build()
 *   好处：字段顺序无关、只设你关心的字段，比构造器更直观。
 * - @Builder 必须配合无参构造器（@NoArgsConstructor）或者单独声明，
 *   否则 Jackson 反序列化时找不到无参构造器会报错。
 *   这里加了 @Builder.Default 的兄弟方案，改用 @Data + @Builder + @NoArgsConstructor + @AllArgsConstructor
 *   来同时满足 Jackson 和 Builder。
 *
 * // v1 改进：v1 直接用 Map<String,String> 传请求体，字段名拼写错误只在运行时才发现。
 * //          v3 用 POJO，IDE 做类型检查，字段名写错编译就报错。
 */
@Data
@Builder
public class LoginReq {
    private String username;
    private String password;
}
