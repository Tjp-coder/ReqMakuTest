package testcase.user;

import api.Org;
import api.User;
import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.FakerUtil;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageTest extends BaseTest {
    private User user = new User();
    private final Integer orgId = 1;
    private Integer id = 0;
    private List<Integer> integers = new ArrayList();
    
    @BeforeAll
    void setUp() {
        //准备前置数据
        //准备一条id为1的机构数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token,defaultData.set("$.id",orgId).json());

        //添加30条数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        for (int i=1;i<=30;i++) {
            defaultData.set("$.id", i);
            defaultData.set("$.orgId",orgId);
            defaultData.set("$.username", "username"+ i);
            defaultData.set("$.mobile",FakerUtil.getTel());
            user.save(token,defaultData.json());
        }
    }

    @BeforeEach
    void readDefault() {
        id++;
        integers.add(id);
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/page.json");
    }

    @AfterAll
    void delete() {
        //每次修改后都进行删除清理
        user.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @Story("分页")
    @DisplayName("分页-正常用例")
    void test02(){
        user.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/user/page.json"));
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
        user.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/user/page.json"));
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
        user.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("分页")
    void test02_false03() {
        user.page("",defaultData.json()).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("信息-正常用例")
    @Story("信息")
    void test03() {
        user.information(token,id).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/user/information.json"));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("信息")
    void test03_false01() {
        user.information("",id).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
