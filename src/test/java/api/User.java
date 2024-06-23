package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 用户管理模块
 * */
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
    public Response importUser(String token, File file) {
        RestAssured.basePath = "/sys/user/import";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .multiPart("file",file)
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

    //导出用户
    @SneakyThrows
    public Response userExport(String token, String resPath) {
        RestAssured.basePath = "/sys/user/export";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .when()
                .get()
                .then().log().all()
                .extract().response();
        //写入到指定文件
        InputStream  inputStream = response.asInputStream();
        OutputStream outputStream = Files.newOutputStream(Paths.get(resPath));
        IOUtils.copy(inputStream,outputStream);
        return response;
    }

}
