package testcase.user;

import api.Org;
import api.User;
import base.BaseTest;
import cn.hutool.json.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.FakerUtil;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("用户管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest extends BaseTest {
    private final User user = new User();
    private final Integer orgId = 1;
    private Integer id = 0;
    private DocumentContext pageData;

    private List<Integer> roleIdList = new ArrayList<>();
    private List<Integer> postIdList = new ArrayList<>();

    @BeforeAll
    void setUp(){
        pageData = JsonPathUtils.jsonPathParseByFile("/template/user/page.json");

        //准备前置数据
        //准备一条id为1的机构
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token,defaultData.set("$.id",orgId).json());
    }

    @BeforeEach
    void readDefault() {
        id++;
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        defaultData.set("$.id", id);
        defaultData.set("$.orgId", orgId);
        //用户名和手机号随机生成，因为这两个字段只能是唯一的
        defaultData.set("$.username", "username"+FakerUtil.getRandomInt(2));
        defaultData.set("$.mobile",FakerUtil.getTel());
    }

    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(id);
        //每次保存后都进行删除清理
        user.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("保存-正常用例")
    void test01() {
        user.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询是否有对应id数据
        if (id != null) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id))
                    .body("data.realName", equalTo(defaultData.read("$.realName")));
        }
    }


    @ParameterizedTest
    @CsvSource({
            "id",
            "password",
            "avatar",
            "email",
            "roleIdList",
            "postIdList",
            "superAdmin",
            "orgName",
            "createTime"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key) {
        //删除key
        Allure.description("删除非必填项" + key + "字段");
        defaultData.delete("$." + key);
        user.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (id != null && !key.equals("id")) {
            String body = user.page(token,pageData.json()).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(id), "列表查询未查到对应id");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "username,用户名不能为空",
            "realName,姓名不能为空",
            "gender,性别不能为空",
            "mobile,手机号不能为空",
            "orgId,机构ID不能为空",
            "status,状态不能为空"
    })
    @DisplayName("删除必填项-异常用例")
    void test01_false02(String key,String expectMsg) {
        //删除key
        Allure.description("删除必填项" + key + "字段");
        defaultData.delete("$." + key);
        user.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (id != null && !key.equals("id")) {
            String body = user.page(token,pageData.json()).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertFalse(idList.contains(id), "列表查询能查到对应id");
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "id",
            "password",
            "avatar",
            "email",
            "roleIdList",
            "postIdList",
            "superAdmin",
            "orgName",
            "createTime"
    })
    @DisplayName("非必填项为空-异常用例")
    void test01_false03(String key) {
        Allure.description("非必填项" + key + "字段为空测试");
        JSONObject json = new JSONObject(defaultData.jsonString());
        defaultData = JsonPathUtils.setNullByType(json.get(key).getClass(),defaultData,key);
        user.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (id != null && !key.equals("id")) {
            String body = user.page(token,pageData.json()).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertTrue(idList.contains(id), "列表查询不能查到对应id");
        }

    }


    @ParameterizedTest
    @CsvSource({
            "username,用户名不能为空",
            "realName,姓名不能为空",
            "gender,性别不能为空",
            "mobile,手机号不能为空",
            "orgId,机构ID不能为空",
            "status,状态不能为空"
    })
    @DisplayName("必填项为空-异常用例")
    void test01_false04(String key,String expectMsg) {
        Allure.description("必填项" + key + "字段为空测试");
        JSONObject json = new JSONObject(defaultData.jsonString());
        defaultData = JsonPathUtils.setNullByType(json.get(key).getClass(),defaultData,key);
        user.save(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //查询列表断言是否包含对应id
        if (id != null && !key.equals("id")) {
            String body = user.page(token,pageData.json()).getBody().asString();
            List<?> idList = JsonPath.read(body, "$.." + "id");
            assertFalse(idList.contains(id), "列表查询能查到对应id");
        }
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false05() {
        user.save("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }
}
