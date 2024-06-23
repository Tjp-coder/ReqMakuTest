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
import static org.junit.jupiter.api.Assertions.*;

@Feature("用户管理模块")
@Story("修改")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest extends BaseTest {
    private User user = new User();
    private final Integer orgId = 1;
    private Integer id = 0;
    private DocumentContext pageData;
    private String expectUserName;
    
    @BeforeAll
    void setUp() {
        //准备分页请求参数，以便后续断言使用
        pageData = JsonPathUtils.jsonPathParseByFile("/template/user/page.json");

        //准备前置数据
        //准备一条id为1的机构数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token,defaultData.set("$.id",orgId).json());

        //添加30条数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        for (int i=1;i<=30;i++) {
            defaultData.set("$.id", i);
            defaultData.set("$.orgId",orgId);
            defaultData.set("$.username", "username"+ FakerUtil.getRandomInt(8));
            defaultData.set("$.mobile",FakerUtil.getTel());
            user.save(token,defaultData.json());
        }
    }

    @BeforeEach
    void readDefault() {
        expectUserName = "username" + FakerUtil.getTimeStamp();
        id++;
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/put.json");
        defaultData.set("$.id", id);
        defaultData.set("$.createTime", currentTime);
        //用户名和手机号随机生成，因为这两个字段只能是唯一的
        defaultData.set("$.username", expectUserName);
        defaultData.set("$.mobile",FakerUtil.getTel());
    }
    
    @AfterEach
    void delete() {
        List<Integer> integers = new ArrayList();
        integers.add(id);
        //每次修改后都进行删除清理
        user.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("修改-正常用例")
    void test01() {
        user.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询详情是否修改了对应数据
        if (id != null) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id));
            String actual = JsonPath.read(user.getResponse().asString(), "$.data.username");
            assertEquals(actual,expectUserName,"未成功修改数据");
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
        user.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询详情是否修改了对应数据
        if (id != null && !key.equals("id")) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id));
            String actual = JsonPath.read(user.getResponse().asString(), "$.data.username");
            assertEquals(actual,expectUserName,"未成功修改数据");
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
        user.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //查询详情是否修改了对应数据
        if (id != null) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id));
            String actual = JsonPath.read(user.getResponse().asString(), "$.data.username");
            assertNotEquals(actual,expectUserName,"成功修改了数据");
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
        user.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());

        //查询详情是否修改了对应数据
        if (id != null && !key.equals("id")) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id));
            String actual = JsonPath.read(user.getResponse().asString(), "$.data.username");
            assertEquals(actual,expectUserName,"未成功修改数据");
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
        user.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());

        //查询详情是否修改了对应数据
        if (id != null) {
            user.information(token, id).then()
                    .assertThat().statusCode(200)
                    .body("data.id", equalTo(id));
            String actual = JsonPath.read(user.getResponse().asString(), "$.data.username");
            assertNotEquals(actual,expectUserName,"成功修改了数据");
        }
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false05() {
        user.put("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }


}
