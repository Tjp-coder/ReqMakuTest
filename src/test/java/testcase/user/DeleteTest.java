package testcase.user;

import api.Org;
import api.User;
import base.BaseTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import util.FakerUtil;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;


@Feature("用户管理模块")
@Story("删除")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTest extends BaseTest {
    private User user = new User();
    private final Integer orgId = 1;
    private Integer id = 0;
    private DocumentContext pageData;

    @BeforeAll
    void setUp() {
        //准备前置数据
        //准备一条id为1的机构数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token,defaultData.set("$.id",orgId).json());

        //添加1条数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        defaultData.set("$.id", 1);
        defaultData.set("$.orgId",orgId);
        defaultData.set("$.username", "username"+ FakerUtil.getTimeStamp());
        defaultData.set("$.mobile",FakerUtil.getTel());
        user.save(token,defaultData.json());

    }

    @BeforeEach
    void readDefault() {
        id++;
        pageData = JsonPathUtils.jsonPathParseByFile("/template/user/page.json");
    }


    @Test
    @DisplayName("删除-正常用例")
    void test01() {
        List<Integer> integers = new ArrayList<>();
        integers.add(id);
        user.delete(token,integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
        //列表查询断言
        //查询列表断言是否有对应id
        List<?> idList;
        String body = user.page(token,pageData.json()).getBody().asString();
        idList = JsonPath.read(body,"$.."+ "id");
        //去数据库验证是否删除此数据
        Map<String, Object> query = jdbcTemplate.queryForMap("SELECT * FROM sys_user WHERE id=" + id);
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
        user.delete(token,integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo("服务器异常，请稍后再试"))
                .body("data", emptyOrNullString());
    }

    @Test
    @Story("删除")
    @DisplayName("删除不存在的值-异常用例")
    void test01_false02() {
        List<Integer> integers = new ArrayList<>();
        user.delete(token,integers).then().assertThat()
                .statusCode(200);
    }

    @Test
    @Story("删除")
    @DisplayName("未授权访问-异常用例")
    void test01_false03() {
        List<Integer> integers = new ArrayList<>();
        user.delete("",integers).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

}
