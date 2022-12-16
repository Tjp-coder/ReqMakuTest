package testcase.post;

import api.Post;
import base.BaseInterface;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Feature("岗位管理模块")
public class PostTest extends BaseTest{
    private Post post = new Post();
    private Integer id = 1;

    @BeforeAll
    void setUp(){
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/post/save.json");
        //添加5条状态为0的数据
        defaultData.set("$.status", 0);
        for (int i=1;i<=5;i++){
            defaultData.set("$.id", i);
            defaultData.set("$.createTime", currentTime);
            post.save(token,defaultData.json()).then().assertThat().statusCode(200);
        }
        //添加5条状态为1的数据
        defaultData.set("$.status", 1);
        for (int i=6;i<=10;i++){
            defaultData.set("$.id", i);
            defaultData.set("$.createTime", currentTime);
            post.save(token,defaultData.json()).then().assertThat().statusCode(200);
        }
    }

    @BeforeEach
    void readDefault() {
        id++;
        //分页数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/post/page.json");
    }

    @Test
    @Story("删除")
    @DisplayName("删除-正常用例")
    void test01() {
        List<Integer> integers = new ArrayList<>();
        integers.add(id);
        post.delete(token,integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //列表查询断言
        //查询列表断言是否有对应id
        List<?> idList;
        String body = post.list(token).getBody().asString();
        idList = JsonPath.read(body,"$.."+ "id");

        //去数据库验证是否删除此数据
        Map<String, Object> query = jdbcTemplate.queryForMap("SELECT * FROM sys_post WHERE id=" + id);
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
        post.delete(token,integers).then().assertThat()
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
        post.delete("",integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/params/post/page.csv",numLinesToSkip = 1)
    @DisplayName("单条件和组合条件查询-正常用例")
    @Story("分页")
    void test02(String description,String postCode,String postName,Integer status) {
        //参数化
        Allure.description(description);
        defaultData.set("$.postCode",postCode);
        defaultData.set("$.postName",postName);
        defaultData.set("$.status",status);
        post.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/post/page.json"));
        //提取断言数据
        List<?> list1 = JsonPath.read(post.getResponse().getBody().asString(), "$..postCode");
        List<?> list2 = JsonPath.read(post.getResponse().getBody().asString(), "$..postName");
        List<?> list3 = JsonPath.read(post.getResponse().getBody().asString(), "$..status");
        //断言
        if (postCode != null){
            for (Object value:list1){
                assertEquals(value,postCode,"未列出指定postCode");
            }
        }
        if (postName != null){
            for (Object value:list2){
                assertEquals(value,postName,"未列出指定postName");
            }
        }
        if (status != null){
            for (Object value:list3){
                assertEquals(value,status,"未列出指定status");
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "order",
            "asc",
            "postCode",
            "postName",
            "status"
    })
    @DisplayName("删除非必填项-异常用例")
    @Story("分页")
    void test02_false01(String key) {
        Allure.description("删除非必填项"+key+"字段");
        defaultData.delete("$." + key);
        post.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/post/page.json"));
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
        post.page(token,defaultData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("分页")
    void test02_false03() {
        post.page("",defaultData.json()).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("信息-正常用例")
    @Story("信息")
    void test03() {
        post.information(token,id).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/post/information.json"));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("信息")
    void test03_false01() {
        post.information("",id).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("列表-正常用例")
    @Story("列表")
    void test04() {
        post.list(token).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/post/list.json"));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("列表")
    void test04_false01() {
        post.list("").then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
