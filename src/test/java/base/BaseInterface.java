package base;

import common.Restful;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 基础接口对象类
 */
public abstract class BaseInterface {
    RequestSpecification specification = RestAssured.requestSpecification;

    protected static String token;
    protected Response response;

    public String JSONTTPE = "application/json";
    public String FORMTYPE = "application/x-www-form-urlencoded";

    public static void setToken(String str) {
        token = str;
    }

    public static String getToken() {
        return token;
    }

    public Response getResponse(){
        return response;
    }


    /**
     * 统一装载请求配置
     * 调用时可以直接通过赋值进行调用,减少赋值的工作量
     * 暂不考虑使用，后期可以数据文件驱动再考虑
     */
    @Deprecated
    public Response run(Restful restful){
        //默认值
        specification.baseUri("http://localhost");
        specification.port(8080);

        //url装载
        if (restful.baseURI != null){
            specification.baseUri(restful.baseURI);
        }
        if (restful.basePath != null){
            specification.basePath(restful.basePath);
        }
        if (restful.port != null){
            specification.port(restful.port);
        }
        //请求头装载
        if (restful.header != null){
            specification.headers(restful.header);
        }
        if (restful.contentType != null){
            specification.contentType(restful.contentType);
        }
        //请求参数装载
        if (restful.pathParams != null){
            specification.pathParams(restful.pathParams);
        }
        if (restful.query != null){
            specification.formParams(restful.query);
        }
        if (restful.body != null){
            specification.body(restful.body);
        }
        if (restful.bodyRaw != null){
            specification.body(restful.bodyRaw);
        }

       return specification.log().all()
               .request(restful.method).then()
               .log().all().extract().response();
    }

}
