package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

/**
 * 机构管理模块
 * */
public class Org extends BaseInterface {

    //列表
    public Response list(String token) {
        RestAssured.basePath = "/sys/org/list";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                        )
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //查看信息
    public Response information(String token, Integer id) {
        RestAssured.basePath = "/sys/org/{id}";
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

    //保存
    public Response save(String token,Map<String,?> map) {
        RestAssured.basePath = "/sys/org";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                        ,"Content-Type","application/json"
                )
                .body(map)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }

    //修改
    public Response put(String token,Map<String,?> map) {
        RestAssured.basePath = "/sys/org";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                        ,"Content-Type","application/json"
                )
                .body(map)
                .when()
                .put()
                .then().log().all()
                .extract().response();
        return response;
    }

    //删除
    public Response delete(String token,Integer id) {
        RestAssured.basePath = "/sys/org/{id}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                        ,"Content-Type","application/json"
                )
                .when()
                .pathParam("id",id)
                .delete()
                .then().log().all()
                .extract().response();
        return response;
    }
}
