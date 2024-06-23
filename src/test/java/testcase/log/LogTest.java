package testcase.log;

import api.Log;
import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import util.JsonPathUtils;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("登录日志模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogTest extends BaseTest {
    private Log log = new Log();

    @BeforeEach
    void setUp() {
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/log/page.json");
    }

    @Test
    @Story("分页")
    @DisplayName("分页-正常用例")
    void test01(){
        log.page(token,defaultData.json()).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/log/page.json"));
    }

    @Test
    @Story("分页")
    @DisplayName("未授权访问")
    void test01_false01(){
        log.page("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @Story("导出excel")
    @DisplayName("导出excel-正常用例")
    void test02(){
        String resPath = RESOURCES_PATH + "/params/log/systemLoginLog.xlsx";
        log.exportExecel(token,resPath).then().assertThat().statusCode(200);
    }


    @Test
    @Story("导出excel")
    @DisplayName("未授权访问")
    void test02_false01(){
        String resPath = RESOURCES_PATH + "/params/log/systemLoginLog.xlsx";
        log.exportExecel("",resPath).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
