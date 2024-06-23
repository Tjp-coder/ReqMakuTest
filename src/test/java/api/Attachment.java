package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件管理模块
 * */
public class Attachment extends BaseInterface {

    //保存
    public Response save(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/attachment";
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

    //删除
    public Response delete(String token, List<Integer> integers) {
        RestAssured.basePath = "/sys/attachment";
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

    //分页
    public Response page(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/attachment/page";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/x-www-form-urlencoded"
                )
                .queryParams(data)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }
}
