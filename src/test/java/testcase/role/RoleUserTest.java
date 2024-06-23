package testcase.role;

import api.Org;
import api.Role;
import api.User;
import base.BaseTest;
import com.jayway.jsonpath.DocumentContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

/**
 * 角色用户
 * */
@Feature("角色管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleUserTest extends BaseTest {
    private Integer id = 1;
    private Role role = new Role();
    private DocumentContext  orgSaveData;
    private DocumentContext  roleSaveData;
    private DocumentContext  userSaveData;
    private DocumentContext userPageData;
    private Map<String,Object> data_scope = new HashMap<>();



    //前置数据
    //角色，用户,机构
    @BeforeAll
    void setUp() {
        //添加一条机构
        orgSaveData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        orgSaveData.set("$.id",1);
        new Org().save(token,orgSaveData.json());

        //添加1条角色数据，角色属于上一条机构里
        roleSaveData = JsonPathUtils.jsonPathParseByFile("/template/role/save.json");
        roleSaveData.set("$.id", 2);
        new Role().save(token,roleSaveData.json());

        //添加一条用户数据
        userSaveData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        userSaveData.set("$.id",3);
        new User().save(token,userSaveData.json());

        orgSaveData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        roleSaveData = JsonPathUtils.jsonPathParseByFile("/template/role/save.json");
        userPageData = JsonPathUtils.jsonPathParseByFile("/template/role/userPageData.json");
        userSaveData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");

        //初始化数据权限参数
        data_scope.put("id",3);
        data_scope.put("dataScope",1);
        ArrayList<Integer> orgIntegers = new ArrayList<>();
        orgIntegers.add(1);
        data_scope.put("orgIdList",orgIntegers);
    }

    @BeforeEach
    void beforeEach(){

    }

    @AfterAll
    void tearDown(){
        ArrayList<Integer> roleIntegers = new ArrayList<>();
        roleIntegers.add(2);
        new Role().delete(token,roleIntegers);
        ArrayList<Integer> userIntegers = new ArrayList<>();
        userIntegers.add(3);
        new User().delete(token,userIntegers);
        new Org().delete(token,1);
    }


    @Story("数据权限")
    @Test
    void test01(){
        Integer dataScope = 0;
        data_scope.put("dataScope",dataScope);
//        role.dataScope(token,data_scope).then().assertThat()
//                .statusCode(200);
    }


    @Story("分配角色给用户列表")
    @Test
    void test02(){
        Integer roleId = 2;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        role.assignRole(token,roleId,list).then().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @Story("删除角色用户")
    @Test
    void test03(){
        Integer roleId = 2;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        role.deleteRole(token,roleId,list).then().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }


    @Story("角色用户-分页")
    @Test
    @DisplayName("角色用户-分页-正常用例")
    void test04() {
        role.userPage(token,userPageData.json()).then().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/role/userPageData.json"));
    }

    @Story("角色用户-分页")
    @ParameterizedTest
    @CsvSource({
            "order",
            "asc",
            "name"
    })
    @DisplayName("删除非必填项-异常用例")
    void test02_false01(String key) {
        Allure.description("删除非必填项"+key+"字段");
        userPageData.delete("$." + key);
        role.userPage(token,userPageData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body(matchesJsonSchemaInClasspath(JSONSCHEMA_PATH+"/user/page.json"));
    }

    @Story("角色用户-分页")
    @ParameterizedTest
    @CsvSource({
            "page,页码不能为空",
            "limit,每页条数不能为空"
    })
    @DisplayName("删除必填项-异常用例")
    void test02_false02(String key,String expectMsg) {
        Allure.description("删除必填项"+key+"字段");
        userPageData.delete("$." + key);
        role.userPage(token,userPageData.json()).then()
                .assertThat().statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data",emptyOrNullString());
    }

    @Story("角色用户-分页")
    @Test
    @DisplayName("未授权访问-异常用例")
    void test02_false03() {
        role.page("",userPageData.json()).then()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }





}
