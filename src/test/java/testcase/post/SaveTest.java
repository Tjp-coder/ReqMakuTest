package testcase.post;

import api.Auth;
import api.Post;
import base.BaseInterface;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Feature("岗位管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest extends BaseTest {
    private Integer saveID = 1;
    private Post post = new Post();

    @BeforeEach
    void readDefault() {
        saveID++;
        //装载模板数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/post/save.json");
        defaultData.set("$.id", saveID);
        defaultData.set("$.createTime", currentTime);
    }

    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(saveID);

        //每个保存后都进行删除
        post.delete(token, integers)
                .then().statusCode(200);
    }

    @ParameterizedTest
    @CsvSource({
        "0",
        "1"
    })
    @DisplayName("保存")
    void test01(Integer status) {
        defaultData.set("$.status", status);
        post.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否有对应id
        if (saveID != null) {
            post.information(token,saveID).then()
                    .assertThat()
                    .body("data.id",equalTo(saveID))
                    .body("data.status",equalTo(status));
            //status为0为禁用状态，不出现在列表查询中
            if(status != 0){
                String body = post.list(token).getBody().asString();
                List<Integer> idList = JsonPath.read(body, "$.." + "id");
                assertTrue(idList.contains(saveID), "列表查询未查到对应id");
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "id",
            "createTime"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key) {
        Allure.description("删除非必填项" + key + "字段");
        String jsonPathKey = "$." + key;
        //删除key
        defaultData.delete(jsonPathKey);
        post.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (saveID != null && !key.equals("id")) {
            String body = post.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(saveID), "列表查询未查到对应id");
        }

    }

    @ParameterizedTest
    @CsvSource({
            "postCode",
            "postName",
            "sort",
            "status"
    })
    @DisplayName("删除必填项-异常用例")
    void test01_false02(String key) {
        Allure.description("删除必填项" + key + "字段");
        String jsonPathKey = "$." + key;
        //删除key
        defaultData.delete(jsonPathKey);
        post.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo("服务器异常，请稍后再试"))
                .body("data", emptyOrNullString());

        //列表查询断言是否不包含对应id
        if (saveID != null) {
            String body = post.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertFalse(idList.contains(saveID), "列表查询查到对应id");
        }
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04() {
        post.save("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
