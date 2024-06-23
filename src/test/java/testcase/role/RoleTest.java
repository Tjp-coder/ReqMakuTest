package testcase.role;

import api.Role;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Feature("角色管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleTest extends BaseTest {
    private Integer id = 1;
    private Role role = new Role();

    @BeforeAll
    void setUp() {
        //添加20条数据
        for (int i=1;i<=20;i++) {
            defaultData = JsonPathUtils.jsonPathParseByFile("/template/role/save.json");
            defaultData.set("$.id", i);
            role.save(token,defaultData.json());
        }
    }

    @BeforeEach
    void readDefault() {
        id++;
        //分页数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/role/page.json");
    }

    @Test
    @Story("删除")
    @DisplayName("删除-正常用例")
    void test01() {
        List<Integer> integers = new ArrayList<>();
        integers.add(id);
        role.delete(token,integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
        //列表查询断言
        //查询列表断言是否有对应id
        List<?> idList;
        String body = role.list(token).getBody().asString();
        idList = JsonPath.read(body,"$.."+ "id");
        //去数据库验证是否删除此数据
        Map<String, Object> query = jdbcTemplate.queryForMap("SELECT * FROM sys_role WHERE id=" + id);
        assertAll(()->{
                    assertFalse(idList.contains(id),"列表中数据中未删除");
                    assertTrue(query.isEmpty(),"数据库中数据未删除");
                }
        );
    }

    @Test
    @Story("删除")
    @DisplayName("参数为空进行删除-异常用例")
    void test01_false01() {
        List<Integer> integers = new ArrayList<>();
        role.delete(token,integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo("服务器异常，请稍后再试"))
                .body("data", emptyOrNullString());
    }

    @Test
    @Story("删除")
    @DisplayName("未授权访问-异常用例")
    void test01_false02() {
        List<Integer> integers = new ArrayList<>();
        role.delete("",integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @Story("分页")
    @DisplayName("分页-正常用例")
    void test02(){
        role.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
              .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/role/page.json"));
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
        role.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/role/page.json"));
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
        role.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("分页")
    void test02_false03() {
        role.page("",defaultData.json()).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("信息-正常用例")
    @Story("信息")
    void test03() {
        role.information(token,id).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/role/information.json"));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("信息")
    void test03_false01() {
        role.information("",id).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("列表-正常用例")
    @Story("列表")
    void test04() {
        role.list(token).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/role/list.json"));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("列表")
    void test04_false01() {
        role.list("").then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
