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
 * 登录日志模块
 * */
public class Log extends BaseInterface {

    //分页
    public Response page(String token, Map<String,?> data) {
        RestAssured.basePath = "/sys/log/login/page";
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

    //导出excel
    @SneakyThrows
    public Response exportExecel(String token, String resPath) {
        RestAssured.basePath = "/sys/log/login/export";
        response = RestAssured.given().log().all()
                .headers("Authorization", token
                )
                .when()
                .get()
                .then().log().all()
                .extract().response();
        InputStream inputStream = response.asInputStream();

        //将下载的文件以二进制流保存写入目标文件
        OutputStream outputStream = new FileOutputStream(resPath);
        IOUtils.copy(inputStream,outputStream);
        return response;
    }
}
