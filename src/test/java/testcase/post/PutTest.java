package testcase.post;

import api.Post;
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

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("岗位管理模块")
@Story("修改")
public class PutTest extends BaseTest {

    private Integer id = 1;

    private Post post = new Post();

    @BeforeEach
    void readDefault() {
        id++;
        //装载模板数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/post/save.json");
        defaultData.set("$.id", id);
        post.save(token,defaultData.json()).then().assertThat().statusCode(200);
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/post/put.json");
        defaultData.set("$.id", id);
        defaultData.set("$.createTime", currentTime);
    }

    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(id);

        //每个修改后都进行删除
        post.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("修改")
    void test01() {
        defaultData.set("$.id", id);
        post.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否有对应id
        if (id != null) {
            String body = post.list(token).getBody().asString();
            List<Integer> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(id), "列表查询未查到对应id");
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
        post.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
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
        String expectMsg = "服务器异常，请稍后再试";
        if (key.equals("postCode")){
            expectMsg = "岗位编码不能为空";
        }else if (key.equals("postName")){
            expectMsg = "岗位名称不能为空";
        }
        Allure.description("删除必填项" + key + "字段");
        String jsonPathKey = "$." + key;
        //删除key
        defaultData.delete(jsonPathKey);
        post.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04() {
        post.put("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
