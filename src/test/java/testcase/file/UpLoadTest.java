package testcase.file;

import api.UpLoad;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("文件上传模块")
@Story("文件上传")
public class UpLoadTest extends BaseTest {
    private UpLoad upLoad = new UpLoad();
    @Test
    @DisplayName("文件上传")
    void test01(){
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        File file = new File(path);
        long fileSize = file.length();
        upLoad.upload(token,file).then()
                .body("msg",equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/file/upload.json"));
        Integer size = JsonPath.read(upLoad.getResponse().asString(),"$.data.size");
        Assertions.assertEquals(fileSize,size.longValue());
    }

    @Test
    @DisplayName("未授权访问")
    void test01_false01(){
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        upLoad.upload("",new File(path)).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
