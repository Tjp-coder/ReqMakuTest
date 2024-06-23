package testcase.user;

import api.Auth;
import api.Org;
import api.User;
import base.BaseInterface;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import util.FakerUtil;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("用户管理模块")
public class UserExportTest extends BaseTest {
    private User user = new User();

    private final Integer orgId = 1;
    private Integer id = 0;
    private List<Integer> integers = new ArrayList();

    @BeforeAll
    void setUp() {
        //准备前置数据
        //准备一条id为1的机构数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token, defaultData.set("$.id", orgId).json());

        //添加5个用户账号
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        for (int i = 1; i <= 5; i++) {
            defaultData.set("$.id", i);
            defaultData.set("$.orgId", orgId);
            defaultData.set("$.username", "username" + i);
            defaultData.set("$.mobile", FakerUtil.getTel());
            user.save(token, defaultData.json());
        }
    }

    @BeforeEach
    void readDefault() {
        id++;
        integers.add(id);
        token = BaseInterface.getToken();
    }

    @AfterEach
    void afterToken() {
        id++;
        integers.add(id);
    }

    @AfterAll
    void delete() {
        user.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("管理员用户进行操作-导出用户")
    @Story("导出用户")
    void test01() {
        Map<String,Object> data = new HashMap<>();
        data.put("username","admin");
        data.put("password","admin");
        new Auth().login(data).then().statusCode(200);

        String resPath = RESOURCES_PATH+ "/params/user/system_user_excel_admin2022-12-20.xls";
        user.userExport(token,resPath).then().assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("普通用户进行操作-导出用户")
    @Story("导出用户")
    void test01_01() {
        //因为从测试基类继承下来的token只会赋值一次，所以本次测试方法需手动再次登录设置并提取出token进行使用
        //切换普通用户登录
        Map<String,Object> data = new HashMap<>();
        data.put("username","username"+"1");
        data.put("password","123456");
        Response login = new Auth().login(data);
        String token = JsonPath.read(login.asString(), "$.data.access_token");

        String resPath = RESOURCES_PATH+ "/params/user/system_user_excel_notExist2022-12-20.xls";
        user.userExport(token,resPath).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(403))
                .body("msg",equalTo("没有权限，禁止访问"))
                .body("data",emptyOrNullString());
    }
}
