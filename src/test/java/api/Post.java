package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位管理模块
 * */
public class Post extends BaseInterface {

    //保存
    public Response save(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/post";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/json"
                )
                .body(data)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }

    //修改
    public Response put(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/post";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/json"
                )
                .body(data)
                .when()
                .put()
                .then().log().all()
                .extract().response();
        return response;
    }

    //删除
    public Response delete(String token, List<Integer> integers) {
        RestAssured.basePath = "/sys/post";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/json"
                )
                .body(integers)
                .when()
                .delete()
                .then().log().all()
                .extract().response();
        return response;
    }

    //信息
    public Response information(String token, Integer id) {
        RestAssured.basePath = "/sys/post/{id}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .pathParam("id",id)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //分页
    public Response page(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/post/page";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .queryParams(data)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //列表
    public Response list(String token) {
        RestAssured.basePath = "/sys/post/list";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }
}
