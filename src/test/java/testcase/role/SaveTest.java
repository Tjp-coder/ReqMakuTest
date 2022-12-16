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
import org.junit.jupiter.params.provider.ValueSource;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("角色管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest extends BaseTest {
    private Integer id = 1;
    private Role role = new Role();

    @BeforeAll
    void setUp() {
    }

    @BeforeEach
    void readDefault() {
        id++;
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/role/save.json");
        defaultData.set("$.id", id);
        defaultData.set("$.createTime", currentTime);
    }

    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(id + 1);
        //每次保存后都进行删除
        role.delete(token, integers)
                .then().statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    @DisplayName("穷举数据范围保存")
    void test01(Integer dataScope) {
        defaultData.set("$.dataScope",dataScope);
        role.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否有对应id
        if (id != null) {
            role.information(token, id).then()
                    .assertThat()
                    .body("data.id", equalTo(id))
                    .body("data.name", equalTo(defaultData.read("$.name")));
            String body = role.list(token).getBody().asString();
            List<Integer> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(id), "列表查询未查到对应id");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "id",
            "remark",
            "dataScope",
            "menuIdList",
            "orgIdList",
            "createTime"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key) {
        //删除key
        Allure.description("删除非必填项" + key + "字段");
        defaultData.delete("$." + key);
        role.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (id != null && !key.equals("id")) {
            String body = role.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(id), "列表查询未查到对应id");
        }

    }

    @ParameterizedTest
    @CsvSource({
            "name,角色名称不能为空"
    })
    @DisplayName("删除必填项-异常用例")
    void test01_false02(String key,String expectMsg) {
        //删除key
        Allure.description("删除必填项" + key + "字段");
        defaultData.delete("$." + key);
        role.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //列表查询断言是否不包含对应id
        if (id != null) {
            String body = role.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertFalse(idList.contains(id), "列表查询查到对应id");
        }
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04() {
        role.save("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
