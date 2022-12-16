package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class User extends BaseInterface {
    //保存
    public Response save(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/json"
                )
                .body(data)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }


    //修改
    public Response put(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/json"
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
        RestAssured.basePath = "/sys/user";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/json"
                )
                .body(integers)
                .when()
                .delete()
                .then().log().all()
                .extract().response();
        return response;
    }

    //修改密码
    public Response putPassword(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user/password";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/json"
                )
                .body(data)
                .when()
                .put()
                .then().log().all()
                .extract().response();
        return response;
    }


    //导入用户
    public Response importUser(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user/import";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/json"
                )
                .queryParams(data)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }


    //信息
    public Response information(String token, Integer id) {
        RestAssured.basePath = "/sys/user/{id}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/x-www-form-urlencoded"
                )
                .pathParams("id", id)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //分页
    public Response page(String token, Map<String, ?> data) {
        RestAssured.basePath = "/sys/user/page";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type", "application/x-www-form-urlencoded"
                )
                .queryParams(data)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //登录用户
    public Response userInfo(String token) {
        RestAssured.basePath = "/sys/user/info";
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
