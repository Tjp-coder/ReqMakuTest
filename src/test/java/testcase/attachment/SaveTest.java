package testcase.attachment;

import api.Attachment;
import api.UpLoad;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;

import java.io.File;


import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;


@Feature("附件管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest extends BaseTest {
    private Attachment attachment = new Attachment();
    private UpLoad upLoad = new UpLoad();

    @BeforeAll
    void setUp(){
    }

    @BeforeEach
    void readDefault() {
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/attachment/save.json");
    }

    @Test
    @DisplayName("保存-正常用例")
    void test01() {
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        File file = new File(path);

        upLoad.upload(token,file);
        String resUpLoad = upLoad.getResponse().asString();
        String filename = JsonPath.read(resUpLoad,"$.data.name");
        String url = JsonPath.read(resUpLoad,"$.data.url");
        Integer size = JsonPath.read(resUpLoad,"$.data.size");
        String platform = JsonPath.read(resUpLoad,"$.data.platform");

        defaultData.set("$.name",filename);
        defaultData.set("$.url",url);
        defaultData.set("$.size",size);
        defaultData.set("$.platform",platform);
        attachment.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @ParameterizedTest
    @CsvSource({
            "id",
            "name",
            "url",
            "size",
            "platform",
            "createTime"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key) {
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        File file = new File(path);

        upLoad.upload(token,file);
        String resUpLoad = upLoad.getResponse().asString();
        String filename = JsonPath.read(resUpLoad,"$.data.name");
        String url = JsonPath.read(resUpLoad,"$.data.url");
        Integer size = JsonPath.read(resUpLoad,"$.data.size");
        String platform = JsonPath.read(resUpLoad,"$.data.platform");

        defaultData.set("$.name",filename);
        defaultData.set("$.url",url);
        defaultData.set("$.size",size);
        defaultData.set("$.platform",platform);

        //删除key
        Allure.description("删除非必填项" + key + "字段");
        defaultData.delete("$." + key);

        attachment.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问")
    void test01_false02(){
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        File file = new File(path);

        upLoad.upload(token,file);
        String resUpLoad = upLoad.getResponse().asString();
        String filename = JsonPath.read(resUpLoad,"$.data.name");
        String url = JsonPath.read(resUpLoad,"$.data.url");
        Integer size = JsonPath.read(resUpLoad,"$.data.size");
        String platform = JsonPath.read(resUpLoad,"$.data.platform");

        defaultData.set("$.name",filename);
        defaultData.set("$.url",url);
        defaultData.set("$.size",size);
        defaultData.set("$.platform",platform);

        attachment.save("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
