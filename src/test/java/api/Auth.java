package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * 认证管理模块
 */
public class Auth extends BaseInterface {

    public Response login(Map<String, ?> loginParams) {
        RestAssured.basePath = "/sys/auth/login";
        response = given().log().all()
                .header("content-type", "application/json;charset=UTF-8")
                .body(loginParams)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        //登录后设置全局变量token
        BaseInterface.setToken(JsonPath.from(response.asString()).getString("data.access_token"));
        return response;
    }

    public Response logout(String token) {
        RestAssured.basePath = "/sys/auth/logout";
        response = given().log().all()
                .headers("Authorization", token)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }

}
