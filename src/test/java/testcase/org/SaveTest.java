package testcase.org;

import api.Org;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;
import util.ParamsUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 保存接口测试
 */
@Feature("机构管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest extends BaseTest {
    private Integer saveID = 1;
    private final Org org = new Org();

    @BeforeAll
    void setUp() {

    }

    @BeforeEach
    void readDefault() {
        saveID++;
        //装载模板数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        defaultData.set("$.id", saveID);
        defaultData.set("$.createTime", currentTime);
    }

    @AfterEach
    void delete() {
        //每个保存后都进行删除
        org.delete(token, saveID)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("不带子机构保存")
    void test01() {
        org.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否有对应id
        if (saveID != null) {
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(saveID), "列表查询未查到对应机构id");
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("带子机构保存")
    void test01_01() {
        //添加子机构
        String data = defaultData.jsonString();
        LinkedHashMap<String, Object> children = defaultData.json();
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        list.add(children);
        Map<String, Object> map = ParamsUtil.strToMap(data);
        map.put("children", list);
        //断言
        org.save(token, map).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否有对应id
        if (saveID != null) {
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertThat(idList.toArray(), hasItemInArray(saveID));
        }

    }

    @ParameterizedTest
    @CsvSource({
            "id",
            "children",
            "createTime",
            "parentName"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key) {
        Allure.description("删除非必填项" + key + "字段");
        //删除key
        defaultData.delete("$." + key);
        org.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (saveID != null && !key.equals("id")) {
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertThat(idList.toArray(), hasItemInArray(saveID));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "pid,上级ID不能为空",
            "name,机构名称不能为空",
            "sort,服务器异常，请稍后再试"
    })
    @DisplayName("删除必填项-异常用例")
    void test01_false02(String key,String expectMsg) {
        Allure.description("删除必填项" + key + "字段");
        //删除key
        defaultData.delete("$." + key);
        org.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());
    }


    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04() {
        org.put("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
