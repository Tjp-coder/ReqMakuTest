package com.maku.apitest.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 通用分页响应壳，对应 MakuBoot 所有分页接口的 data 字段结构 {total, list}。
 * 使用泛型 T 表示 list 元素类型，配合 CommonResp<PageResult<T>> 解析完整分页响应。
 *
 * 示例：CommonResp<PageResult<SysUserVO>> resp = response.as(new TypeRef<>(){});
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResult<T> {
    private Long total;
    private List<T> list;
}
