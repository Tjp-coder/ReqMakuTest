package com.maku.apitest.api;


import com.maku.apitest.client.RequestSpecFactory;
import io.restassured.specification.RequestSpecification;

/*
 * 职责：所有业务 API 类（AuthApi、UserApi、OrgApi…）的抽象基类，
 *       提供统一的、配置完整的 RequestSpecification 获取入口。
 *
 * 核心概念：
 * - 模板方法模式：BaseApi 定义了"怎么获得请求规格"（req/unauthReq），
 *   子类只需关心"这个接口的路径和参数是什么"，不需要关心 baseUri/token/Filter 怎么配置。
 * - 依赖注入：RequestSpecFactory 通过构造器传入，而不是在类内部 new。
 *   这样测试时可以注入指向不同环境的 factory，不需要修改业务 API 类本身。
 * - 为什么是 abstract class 而不是 interface：
 *   API 类需要持有 factory 状态字段，abstract class 可以有字段，interface 不能。
 *
 * // v1 改进：v1 的 api/User.java 继承 BaseInterface，BaseInterface 里有 protected Response response 字段，
 * //          导致 API 类和响应解析逻辑耦合在一起（API 方法调完就把 response 塞进字段）。
 * //          v3 的 API 方法直接 return Response，测试类拿到后自己做断言，职责分离更清晰。
 */
public abstract class BaseApi {

    protected final RequestSpecFactory factory;

    protected BaseApi(RequestSpecFactory factory) {
        this.factory = factory;
    }

    /**
     * 获取带 Authorization Header 的请求规格，用于需要登录才能访问的接口。
     * 为什么不直接暴露 factory：让子类 API 不需要知道 factory 的存在，只调用语义更明确的 req(token)。
     */
    protected RequestSpecification req(String token) {
        return factory.auth(token);
    }

    /** 获取无鉴权请求规格，用于登录接口等公开接口 */
    protected RequestSpecification unauthReq() {
        return factory.unauth();
    }
}
