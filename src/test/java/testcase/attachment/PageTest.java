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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("附件管理模块")
@Story("分页")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageTest extends BaseTest {
    private Attachment attachment = new Attachment();
    private UpLoad upLoad = new UpLoad();
    private Integer id = 1;
    private String document = "test.xlsx";

    @BeforeAll
    void setUp(){
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/attachment/save.json");
        //准备一些数据
        String path = RESOURCES_PATH+"/params/file/"+ document;
        File file = new File(path);

        upLoad.upload(token,file);
        String resUpLoad = upLoad.getResponse().asString();
        String filename = JsonPath.read(resUpLoad,"$.data.name");
        String url = JsonPath.read(resUpLoad,"$.data.url");
        Integer size = JsonPath.read(resUpLoad,"$.data.size");
        String platform = JsonPath.read(resUpLoad,"$.data.platform");

        defaultData.set("$.id",id);
        defaultData.set("$.name",filename);
        defaultData.set("$.url",url);
        defaultData.set("$.size",size);
        defaultData.set("$.platform",platform);
        attachment.save(token, defaultData.json());
    }

    @BeforeEach
    void beforeEach(){
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/attachment/page.json");
    }

    @Test
    @DisplayName("分页")
    void test01(){
        attachment.page(token,defaultData.json()).then().assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/attachment/page.json"));
    }

    @ParameterizedTest
    @CsvSource({
            "order",
            "asc",
            "name"
    })
    @DisplayName("删除非必填项-异常用例")
    @Story("分页")
    void test02_false01(String key) {
        Allure.description("删除非必填项"+key+"字段");
        defaultData.delete("$." + key);
        attachment.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"));
    }

    @ParameterizedTest
    @CsvSource({
            "page,页码不能为空",
            "limit,每页条数不能为空"
    })
    @DisplayName("删除必填项-异常用例")
    @Story("分页")
    void test02_false02(String key,String expectMsg) {
        Allure.description("删除必填项"+key+"字段");
        defaultData.delete("$." + key);
        attachment.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false01() {
        attachment.page("",defaultData.json()).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
