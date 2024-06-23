package testcase.org;

import api.Auth;
import api.Org;
import base.BaseInterface;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import util.JsonPathUtils;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Feature("机构管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrgTest extends BaseTest {
    private Integer id = 1 ;

    @BeforeAll
    void setUp(){
        //重置数据库中的机构数据
        jdbcTemplate.update("delete from sys_org");
        //插入一条数据
        new Org().save(token,
                JsonPathUtils.jsonPathParseByFile("/template/org/save.json").set("$.id",id).json());
    }

    @Test
    @DisplayName("列表-正常用例")
    @Story("列表")
    void test01(){
        Org org = new Org();
        org.list(token);
        org.getResponse().then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH + "/org/orglist.json"));
    }

    @Test
    @DisplayName("未授权访问")
    @Story("列表")
    void test01_false01(){
        Org org = new Org();
        org.list("").then().assertThat()
                .statusCode(200)
                .body("code",equalTo(401))
                .body("msg",equalTo("还未授权，不能访问"))
                .body("data",emptyOrNullString());

    }

    @Test
    @DisplayName("信息-正常用例")
    @Story("信息")
    void test02(){
        Org org = new Org();
        org.information(token,id).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH + "/org/orgid.json"));
    }

    @Test
    @DisplayName("未授权访问")
    @Story("信息")
    void test02_false01(){
        Org org = new Org();
        org.information("",id).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(401))
                .body("msg",equalTo("还未授权，不能访问"))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("删除-正常用例")
    @Story("删除")
    void test03(){
        Org org = new Org();
        org.delete(token,id).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body("data",emptyOrNullString());

        //列表查询断言
        //查询列表断言是否有对应id
        List<?> idList;
        String body = org.list(token).getBody().asString();
        idList = JsonPath.read(body,"$.."+ "id");

        //去数据库验证是否删除此数据
        Map<String, Object> query = jdbcTemplate.queryForMap("SELECT*FROM sys_org WHERE id=" + id);
        assertAll(()->{
                    assertFalse(idList.contains(id),"列表中数据中未删除");
                    assertTrue(query.isEmpty(),"数据库中数据未删除");
                }
        );
    }

    @Test
    @DisplayName("未授权访问")
    @Story("删除")
    void test03_false01(){
        Org org = new Org();
        org.delete("",id).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(401))
                .body("msg",equalTo("还未授权，不能访问"))
                .body("data",emptyOrNullString());
    }
}
