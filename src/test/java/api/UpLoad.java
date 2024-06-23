package api;

import base.BaseInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

/**
 * 文件上传模块
 * */
public class UpLoad extends BaseInterface {
    //文件上传
    public Response upload(String token, File file) {
        RestAssured.basePath = "/sys/file/upload";
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
}
