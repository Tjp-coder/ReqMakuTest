package com.maku.apitest.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
/*
 * 职责：以流式 API 封装 JsonPath，用于加载 JSON 请求模板并按需修改字段。
 *
 * 核心概念：
 * - JSON 模板技术（v1 继承下来的核心思路）：
 *   把请求体存为 JSON 文件（template/user/save.json），测试时读取后按需改几个字段。
 *   优点：正常用例直接用模板；异常用例用 .set()/.delete() 改几个字段，代码非常简洁。
 * - DocumentContext：JsonPath 库的核心类，代表一个内存中可读写的 JSON 文档。
 *   读：ctx.read("$.username") → 拿到字段值
 *   改：ctx.set("$.username", "new_value") → 修改字段
 *   删：ctx.delete("$.username") → 整个字段从 JSON 中消失（不同于 set null）
 * - 流式 API（Fluent Interface）：每个操作方法返回 this，支持链式调用。
 *   目的是减少局部变量，让测试代码读起来像自然语言。
 *
 * // v1 改进：v1 的 JsonPathUtils.jsonPathParseByFile() 直接返回 DocumentContext，
 * //          测试类拿到后直接调用 ctx.set()、ctx.json() 等底层 API。
 * //          v3 封装成 JsonTemplateUtil，API 更语义化，也隐藏了 "怎么加载文件" 的细节。
 */
public class JsonTemplateUtil {

    private final DocumentContext ctx;

    private JsonTemplateUtil(DocumentContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 从 classpath 加载 JSON 模板文件。
     * 路径相对于 src/test/resources/，例如 "template/user/save.json"。
     *
     * 为什么用 ClassLoader 而不是 new File()：
     * ClassLoader 在 Maven 构建和 IDE 运行时都能正确找到 resources 目录，
     * new File("src/test/resources/...") 则依赖当前工作目录，CI 里经常出问题。
     */
    public static JsonTemplateUtil load(String classpathPath) {
        try (InputStream is = JsonTemplateUtil.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (is == null) {
                throw new IllegalArgumentException("模板文件不存在于 classpath: " + classpathPath);
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new JsonTemplateUtil(JsonPath.parse(json));
        } catch (IOException e) {
            throw new RuntimeException("加载模板文件失败: " + classpathPath, e);
        }
    }

    /**
     * 设置字段值，path 为 JsonPath 表达式，如 "$.username" 或 "$.address.city"。
     * 返回 this 支持链式调用。
     */
    public JsonTemplateUtil set(String path, Object value) {
        ctx.set(path, value);
        return this;
    }

    /**
     * 从 JSON 中完整移除字段，用于测试"必填项缺失"场景。
     *
     * 为什么用 delete 而不是 set(path, null)：
     * set null 会保留字段但值为 null（{"username": null}），
     * delete 让字段完全消失（{}），两者在服务端通常产生不同的校验结果。
     *
     * // v1 改进：v1 也用了 delete()，这是从 v1 继承下来的好技巧。
     */
    public JsonTemplateUtil delete(String path) {
        ctx.delete(path);
        return this;
    }

    /**
     * 转为 JSON 字符串，传给 RestAssured 的 .body()。
     * 示例：given().body(util.toJson()).when().post(...)
     */
    public String toJson() {
        return ctx.jsonString();
    }

    /**
     * 转为 Map<String, Object>，传给 RestAssured 的 .queryParams()。
     * 适用于 GET 请求的查询参数。
     * 示例：given().queryParams(util.toMap()).when().get(...)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return ctx.read("$");
    }
}
