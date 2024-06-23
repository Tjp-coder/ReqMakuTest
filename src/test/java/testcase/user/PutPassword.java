package testcase.user;

import api.Auth;
import api.Org;
import api.User;
import base.BaseTest;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import util.FakerUtil;
import util.JsonPathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("用户管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutPassword extends BaseTest {
    private User user = new User();
    private final Integer orgId = 1;
    private Integer id = 0;
    private List<Integer> integers = new ArrayList();
    private String password;
    private String newPassword;


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
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/password.json");
        password = defaultData.read("$.password");
        newPassword = defaultData.read("$.newPassword");
    }

    @AfterAll
    void delete() {
        //删除新增的用户并将修改后新密码恢复成原先的密码
        user.delete(token, integers)
                .then().statusCode(200);
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/auth/login.json");
        defaultData.set("$.password", newPassword);
        new Auth().login(defaultData.json());
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/password.json");
        defaultData.set("$.password", newPassword);
        defaultData.set("$.newPassword", password);
        user.putPassword(token, defaultData.json());
    }

    @Test
    @DisplayName("修改密码")
    @Story("修改密码")
    void test01() {
        user.putPassword(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }


    @Test
    @DisplayName("未鉴权访问")
    @Story("修改密码")
    void test01_false01() {
        user.putPassword("", defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("登录用户")
    @Story("登录用户")
    void test02() {
        Map<String,Object> login = new HashMap<>();
        login.put("username","username1");
        login.put("password","123456");
        new Auth().login(login);
        user.userInfo(token).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"));
    }
}
