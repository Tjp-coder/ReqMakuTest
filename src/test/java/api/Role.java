package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

/**
 * 角色管理模块
 * */
public class Role extends BaseInterface {
    //保存
    public Response save(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/role";
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
        RestAssured.basePath = "/sys/role";
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
        RestAssured.basePath = "/sys/role";
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

    //数据权限
    public Response dataScope(String token,Map<String,?> data) {
        RestAssured.basePath = "/sys/data-scope";
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

    //分配角色给用户列表
    public Response assignRole(String token, Integer roleId,List<Integer> integers) {
        RestAssured.basePath = "/sys/role/user/{roleId}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/json"
                )
                .pathParam("roleId",roleId)
                .body(integers)
                .when()
                .post()
                .then().log().all()
                .extract().response();
        return response;
    }

    //删除角色用户
    public Response deleteRole(String token, Integer roleId, List<Integer> integers) {
        RestAssured.basePath = "/sys/role/user/{roleId}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/json"
                )
                .pathParam("roleId",roleId)
                .body(integers)
                .when()
                .delete()
                .then().log().all()
                .extract().response();
        return response;
    }

    //信息
    public Response information(String token, Integer id) {
        RestAssured.basePath = "/sys/role/{id}";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/x-www-form-urlencoded"
                )
                .pathParam("id",id)
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }

    //角色用户-分页
    public Response userPage(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/role/user/page";
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

    //分页
    public Response page(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/role/page";
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

    //角色菜单
    public Response menu(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/role/menu";
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

    //列表
    public Response list(String token) {
        RestAssured.basePath = "/sys/role/list";
        response = RestAssured.given().log().all()
                .headers("Authorization", token,
                        "Content-Type","application/x-www-form-urlencoded"
                )
                .when()
                .get()
                .then().log().all()
                .extract().response();
        return response;
    }
}
