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
import static org.junit.jupiter.api.Assertions.*;

@Feature("角色管理模块")
@Story("修改")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest extends BaseTest {
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
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/role/put.json");
        defaultData.set("$.id", id);
        defaultData.set("$.createTime", currentTime);
    }

    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(id);
        //每次保存后都进行删除
        role.delete(token, integers)
                .then().statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    @DisplayName("穷举数据范围修改")
    void test01(Integer dataScope) {
        defaultData.set("$.dataScope",dataScope);
        role.put(token, defaultData.json()).then().assertThat()
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
        String name = "应用管理员2";
        defaultData.set("$.name",name);
        //删除key
        Allure.description("删除非必填项" + key + "字段");
        defaultData.delete("$." + key);
        role.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应name
        String body = role.list(token).getBody().asString();
        List<?> idList = JsonPath.read(body, "$.." + "name");
        assertTrue(idList.contains(name), "列表查询未查到对应姓名");
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
        role.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //断言是否不包含指定创建时间
        List<String> except = JsonPath.read(role.list(token).getBody().asString(), "$.." + "createTime");
        for (String value:except){
            assertNotEquals(value,currentTime);
        }
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04() {
        role.put("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}